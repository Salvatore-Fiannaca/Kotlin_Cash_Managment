package com.cashmanagment.fragments

import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cashmanagment.database.DatabaseHandler
import com.cashmanagment.databinding.FragmentDashboardBinding
import com.cashmanagment.utils.Utils
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry


class Dashboard : Fragment() {

    private lateinit var binding: FragmentDashboardBinding
    private var counter: Int = 0
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
        var cleaned= utils.cleanIntToString(counter)
        binding.dashCount.text = "€ $cleaned"
        setupCharts()
    }

    private fun setupCharts() {
        if (counter > 0) {
            pieChart?.visibility = View.VISIBLE
        } else {
            pieChart?.visibility = View.GONE
        }

        val pieEntries: ArrayList<PieEntry> = ArrayList()
        val pieEntriesTips: ArrayList<PieEntry> = ArrayList()
        val typeAmountMap: MutableMap<String, Double> = HashMap()
        val typeAmountMapTips: MutableMap<String, Int> = HashMap()

        typeAmountMap["Necessità"] = counter.toDouble() / 100 * 55
        typeAmountMapTips["Necessità"] = 55
        typeAmountMap["Risparmio"] = counter.toDouble() / 100 * 10
        typeAmountMapTips["Risparmio"] = 10
        typeAmountMap["Investimento"] = counter.toDouble() / 100 * 10
        typeAmountMapTips["Investimento"] = 10
        typeAmountMap["Formazione"] = counter.toDouble() / 100 * 10
        typeAmountMapTips["Formazione"] = 10
        typeAmountMap["Svago"] = counter.toDouble() / 100 * 10
        typeAmountMapTips["Svago"] = 10
        typeAmountMap["Donazione"] = counter.toDouble() / 100 * 5
        typeAmountMapTips["Donazione"] = 5

        val colors = ArrayList<Int>()
        colors.add(Color.parseColor("#4A545D")) //Risparmio
        colors.add(Color.parseColor("#d8b00f")) //Investimento
        colors.add(Color.parseColor("#68ba43")) //Necessità
        colors.add(Color.parseColor("#652b9b")) //Donazione
        colors.add(Color.parseColor("#134ba0")) //Formazione
        colors.add(Color.parseColor("#ff4444")) //Svago

        for (type in typeAmountMap.keys) {
            pieEntries.add(PieEntry(typeAmountMap[type]!!.toFloat(), type))
            pieEntriesTips.add(PieEntry(typeAmountMapTips[type]!!.toFloat(), type))
        }

        val pieDataSet = PieDataSet(pieEntries, "type")
        val pieDataSetTips = PieDataSet(pieEntriesTips, "type")
        pieDataSet.valueTextSize = 15f
        pieDataSetTips.valueTextSize = 15f
        pieDataSet.valueTextColor = Color.WHITE
        pieDataSetTips.valueTextColor = Color.WHITE
        pieDataSet.colors = colors
        pieDataSetTips.colors = colors

        val pieData = PieData(pieDataSet)
        val pieDataTips = PieData(pieDataSetTips)
        pieDataTips.setDrawValues(false)
        pieChart?.data = pieData
        pieChartTips?.data = pieDataTips
        pieChart?.invalidate()
        pieChartTips?.invalidate()

        // OTHER SETTINGS
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

        pieChart?.centerText = "Current Partition"
        pieChartTips?.centerText = "Recommended Partition"
        pieChart?.setCenterTextSize(17f)
        pieChartTips?.setCenterTextSize(17f)
        pieChart?.setCenterTextColor(Color.WHITE)
        pieChartTips?.setCenterTextColor(Color.WHITE)

    }
}