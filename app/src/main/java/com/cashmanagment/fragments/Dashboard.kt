package com.cashmanagment.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cashmanagment.database.DatabaseHandler
import com.cashmanagment.databinding.FragmentDashboardBinding
import com.cashmanagment.models.HistoryModel
import com.cashmanagment.utils.Utils
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry


class Dashboard : Fragment() {

    private lateinit var binding: FragmentDashboardBinding
    private var counter: Int = 0
    private var counterOut: Int = 0
    private var pieChart: PieChart? = null
    private var pieChartTips: PieChart? = null
    private val utils = Utils()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentDashboardBinding.inflate(layoutInflater)
        pieChart = binding.pieChartView
        pieChartTips = binding.pieChartViewTips
        refreshValues()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        refreshValues()
    }

    private fun getCashAvailable(): Int {
        val dbHandler = DatabaseHandler(this.requireContext())
        return dbHandler.getCounter()
    }
    private fun refreshValues(){
        counter = getCashAvailable()
        counterOut = filterCountOut(getActions())
        var cleaned = utils.cleanIntToString(counter)
        binding.dashCount.text = "€ $cleaned"
        initCharts()
    }
    private fun initCharts(){
        // CHECK FIRST TIME
        if (counterOut > 0) {
            pieChart?.visibility = View.VISIBLE
        } else {
            pieChart?.visibility = View.GONE
        }

        val pieEntries: ArrayList<PieEntry> = ArrayList()
        val pieEntriesTips: ArrayList<PieEntry> = ArrayList()

        val typeAmountMap: MutableMap<String, Double> = HashMap()
        val typeAmountMapTips: MutableMap<String, Int> = HashMap()
        typeAmountMapTips["Necessary"] = 55
        typeAmountMapTips["Risparmio"] = 10
        typeAmountMapTips["Investimento"] = 10
        typeAmountMapTips["Formazione"] = 10
        typeAmountMapTips["Svago"] = 10
        typeAmountMapTips["Donazione"] = 5

        for (type in typeAmountMapTips.keys) {
            pieEntriesTips.add(PieEntry(typeAmountMapTips[type]!!.toFloat(), type))
        }

        val colors = ArrayList<Int>()
        val colorsTips = ArrayList<Int>()
        colorsTips.add(Color.parseColor("#4A545D")) //Risparmio
        colorsTips.add(Color.parseColor("#d8b00f")) //Investimento
        colorsTips.add(Color.parseColor("#68ba43")) //Necessità
        colorsTips.add(Color.parseColor("#652b9b")) //Donazione
        colorsTips.add(Color.parseColor("#134ba0")) //Formazione
        colorsTips.add(Color.parseColor("#ff4444")) //Svago

        val items = getActions()
        val totalOut = filterCountOut(items)
        val tags = arrayListOf("Necessary","Risparmio","Investimento","Formazione","Svago","Donazione")
        for (tag in tags) {
            val value =  filterCountOutBy(items, tag) / totalOut * 100
            if (value > 0) {
                typeAmountMap[tag] = value
                when(tag){
                    "Necessary" -> colors.add(Color.parseColor("#68ba43"))
                    "Risparmio" -> colors.add(Color.parseColor("#4A545D"))
                    "Investimento" -> colors.add(Color.parseColor("#d8b00f"))
                    "Formazione" -> colors.add(Color.parseColor("#134ba0"))
                    "Svago" -> colors.add(Color.parseColor("#ff4444"))
                    "Donazione" -> colors.add(Color.parseColor("#652b9b"))
                }
            }
        }

        for (type in typeAmountMap.keys) {
            pieEntries.add(PieEntry(typeAmountMap[type]!!.toFloat(), type))
        }

        val pieDataSet = PieDataSet(pieEntries, "type")
        val pieDataSetTips = PieDataSet(pieEntriesTips, "type")
        pieDataSet.valueTextSize = 18f
        pieDataSetTips.valueTextSize = 18f
        pieDataSet.valueTextColor = Color.WHITE
        pieDataSetTips.valueTextColor = Color.WHITE
        pieDataSet.colors = colors
        pieDataSetTips.colors = colorsTips

        val pieData = PieData(pieDataSet)
        val pieDataTips = PieData(pieDataSetTips)
        pieData.setDrawValues(false)
        pieDataTips.setDrawValues(false)
        pieChart?.data = pieData
        pieChartTips?.data = pieDataTips
        pieChart?.invalidate()
        pieChartTips?.invalidate()

        settingsCharts()

    }
    private fun settingsCharts(){
        pieChart?.setUsePercentValues(false)
        pieChartTips?.setUsePercentValues(true)
        pieChart?.description?.isEnabled = false
        pieChartTips?.description?.isEnabled = false
        pieChart?.legend?.isEnabled = false
        pieChartTips?.legend?.isEnabled = false
        pieChart?.isRotationEnabled = true
        pieChartTips?.isRotationEnabled = true
        pieChart?.dragDecelerationFrictionCoef = 0.9f
        pieChartTips?.dragDecelerationFrictionCoef = 0.9f
        pieChart?.rotationAngle = 0F
        pieChartTips?.rotationAngle = 0F
        pieChart?.isHighlightPerTapEnabled = true
        pieChartTips?.isHighlightPerTapEnabled = true
        pieChart?.animateY(1200, Easing.EaseInOutQuad)
        pieChartTips?.animateY(1200, Easing.EaseInOutQuad)
        pieChart?.setHoleColor(Color.parseColor("#000000"))
        pieChartTips?.setHoleColor(Color.parseColor("#000000"))

        pieChart?.centerText = "Current"
        pieChartTips?.centerText = "Recommended"
        pieChart?.setCenterTextSize(17f)
        pieChartTips?.setCenterTextSize(17f)
        pieChart?.setCenterTextColor(Color.WHITE)
        pieChartTips?.setCenterTextColor(Color.WHITE)
    }
    private fun filterCountOut( items: ArrayList<HistoryModel>): Int {
        var total = 0;
        val filterByIn  = items.filter { i -> i.type == "out" }
        for (i in filterByIn) total += kotlin.math.abs(i.amount)
        return total
    }
    private fun filterCountOutBy(
            items: ArrayList<HistoryModel>, tag: String): Double {
        var total = 0.0;
        val filterByIn  = items.filter { i -> i.tag == tag }
        for (i in filterByIn) total += kotlin.math.abs(i.amount)
        return total
    }
    private fun getActions(): ArrayList<HistoryModel> {
        val dbHandler = DatabaseHandler(this.requireContext())
        return dbHandler.readAll()
    }
}