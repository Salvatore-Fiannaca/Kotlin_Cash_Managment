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
    private var counter: String = "0"
    private var counterOut: Int = 0
    private var pieChart: PieChart? = null
    private var pieChartTips: PieChart? = null
    private val utils = Utils()
    private var privacyToggle = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentDashboardBinding.inflate(layoutInflater)
        pieChart = binding.pieChartView
        pieChartTips = binding.pieChartViewTips
        setPrivacyMode(true)
        refreshValues()

        binding.privacyToggleHide.setOnClickListener{
            setPrivacyMode(true)
        }
        binding.privacyToggleShow.setOnClickListener{
            setPrivacyMode(false)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        refreshValues()
        setPrivacyMode(privacyToggle)
    }

    private fun getCashAvailable(): String {
        val dbHandler = DatabaseHandler(this.requireContext())
        val rowCounter = dbHandler.getCounter()
        return utils.cleanIntToString(rowCounter)
    }
    private fun refreshValues(){
        counter = getCashAvailable()
        counterOut = filterCountOut(getActions())
        initCharts()
    }
    private fun setPrivacyMode(bool: Boolean){
        if (bool) {
            privacyToggle = true
            binding.dashCount.text = "***"
            binding.privacyToggleShow.visibility = View.VISIBLE
            binding.privacyToggleHide.visibility = View.GONE
        } else {
            privacyToggle = false
            binding.dashCount.text = "€ $counter"
            binding.privacyToggleHide.visibility = View.VISIBLE
            binding.privacyToggleShow.visibility = View.GONE

        }
    }
    private fun initCharts(){
        if (counterOut > 0) {
            pieChart?.visibility = View.VISIBLE
        } else {
           pieChart?.visibility = View.GONE
        }

        val pieEntries: ArrayList<PieEntry> = ArrayList()
        val pieEntriesTips: ArrayList<PieEntry> = ArrayList()

        val typeAmountMap: MutableMap<String, Double> = HashMap()
        val typeAmountMapTips: MutableMap<String, Int> = HashMap()
        val colors = ArrayList<Int>()
        val colorsTips = ArrayList<Int>()

        typeAmountMapTips["Necessary"] = 55
        typeAmountMapTips["Saving"] = 10
        typeAmountMapTips["Investment"] = 10
        typeAmountMapTips["Formation"] = 10
        typeAmountMapTips["Fun"] = 10
        typeAmountMapTips["Donation"] = 5

        colorsTips.add(Color.parseColor("#68ba43")) // Necessary
        colorsTips.add(Color.parseColor("#d8b00f")) // Investment
        colorsTips.add(Color.parseColor("#652b9b")) // Donation
        colorsTips.add(Color.parseColor("#4A545D")) // Saving
        colorsTips.add(Color.parseColor("#134ba0")) // Formation
        colorsTips.add(Color.parseColor("#ff4444")) // Fun

        for (type in typeAmountMapTips.keys) {
            pieEntriesTips.add(PieEntry(typeAmountMapTips[type]!!.toFloat(), type))
        }

        val items = getActions()
        val totalOut = filterCountOut(items)
        for (tag in utils.getTags()) {
            val value =  filterCountOutBy(items, tag) / totalOut * 100
            if (value > 0)
                typeAmountMap[tag] = value
        }
        for (type in typeAmountMap.keys) {
            when(type){
                "Necessary" -> colors.add(Color.parseColor("#68ba43"))
                "Saving" -> colors.add(Color.parseColor("#4A545D"))
                "Investment" -> colors.add(Color.parseColor("#d8b00f"))
                "Formation" -> colors.add(Color.parseColor("#134ba0"))
                "Fun" -> colors.add(Color.parseColor("#ff4444"))
                "Donation" -> colors.add(Color.parseColor("#652b9b"))
            }
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
        pieChart?.setHoleColor(Color.BLACK)
        pieChartTips?.setHoleColor(Color.BLACK)

        pieChart?.centerText = "Current"
        pieChartTips?.centerText = "Recommended"
        pieChart?.setCenterTextSize(17f)
        pieChartTips?.setCenterTextSize(17f)
        pieChart?.setCenterTextColor(Color.WHITE)
        pieChartTips?.setCenterTextColor(Color.WHITE)
    }
    private fun filterCountOut( items: ArrayList<HistoryModel>): Int {
        var total = 0
        val filterByIn  = items.filter { i -> i.type == "out" }
        for (i in filterByIn) total += kotlin.math.abs(i.amount)
        return total
    }
    private fun filterCountOutBy(
            items: ArrayList<HistoryModel>, tag: String): Double {
        var total = 0.0
        val filterByIn  = items.filter { i -> i.tag == tag }
        for (i in filterByIn) total += kotlin.math.abs(i.amount)
        return total
    }
    private fun getActions(): ArrayList<HistoryModel> {
        val dbHandler = DatabaseHandler(this.requireContext())
        return dbHandler.readAll()
    }
}