package com.cashmanagment.fragments

import android.graphics.Color
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
    private val utils = Utils()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentDashboardBinding.inflate(layoutInflater)
        pieChart = binding.pieChartView
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
        showPieChart()
    }

    private fun showPieChart() {
        if (counter > 0) {
            pieChart?.visibility = View.VISIBLE
        } else {
            pieChart?.visibility = View.GONE
            return
        }

        val pieEntries: ArrayList<PieEntry> = ArrayList()
        val typeAmountMap: MutableMap<String, Double> = HashMap()

        typeAmountMap["Necessità"] = counter.toDouble() / 100 * 55
        typeAmountMap["Risparmio"] = counter.toDouble() / 100 * 10
        typeAmountMap["Investimento"] = counter.toDouble() / 100 * 10
        typeAmountMap["Formazione"] = counter.toDouble() / 100 * 10
        typeAmountMap["Svago"] = counter.toDouble() / 100 * 10
        typeAmountMap["Donazione"] = counter.toDouble() / 100 * 5

        val colors = ArrayList<Int>()
        colors.add(Color.parseColor("#4A545D")) //Risparmio
        colors.add(Color.parseColor("#d8b00f")) //Investimento
        colors.add(Color.parseColor("#68ba43")) //Necessità
        colors.add(Color.parseColor("#652b9b")) //Donazione
        colors.add(Color.parseColor("#134ba0")) //Formazione
        colors.add(Color.parseColor("#ff4444")) //Svago

        for (type in typeAmountMap.keys) {
            pieEntries.add(PieEntry(typeAmountMap[type]!!.toFloat(), type))
        }

        //collecting the entries with label name
        val pieDataSet = PieDataSet(pieEntries, "type")
        pieDataSet.valueTextSize = 14f
        pieDataSet.valueTextColor = Color.WHITE
        pieDataSet.colors = colors

        //grouping the data set from entry to chart
        val pieData = PieData(pieDataSet)
        //showing the value of the entries, default true if not set
        pieData.setDrawValues(true)

        pieChart?.data = pieData
        pieChart?.invalidate()

        // OTHER SETTINGS
        pieChart?.setUsePercentValues(false)
        pieChart?.description?.isEnabled = false
        pieChart?.legend?.isEnabled = false
        pieChart?.isRotationEnabled = true
        pieChart?.dragDecelerationFrictionCoef = 0.9f
        pieChart?.rotationAngle = 0F
        pieChart?.isHighlightPerTapEnabled = true
        pieChart?.animateY(1200, Easing.EaseInOutQuad)
        pieChart?.setHoleColor(Color.parseColor("#000000"))
    }
}