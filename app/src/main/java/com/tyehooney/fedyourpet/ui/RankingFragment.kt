package com.tyehooney.fedyourpet.ui

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.tyehooney.fedyourpet.R
import com.tyehooney.fedyourpet.databinding.FragmentRankingBinding

class RankingFragment : Fragment(R.layout.fragment_ranking) {
    private var _binding: FragmentRankingBinding? = null
    private val binding get() = _binding!!
    private val memberList =
        listOf(Member("아빠", 1, 2), Member("엄마", 2, 3), Member("아들", 3, 5), Member("딸", 4, 2))
    private val entries = arrayListOf<BarEntry>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRankingBinding.inflate(inflater, container, false)
        val view = binding.root
        val chart = binding.rankingBarchart
        val labels = arrayListOf<String>()
        memberList.forEachIndexed { idx, it ->
            entries.add(
                BarEntry(
                    (idx).toFloat() + 0.5F,
                    floatArrayOf(it.dogFood.toFloat(), it.catFood.toFloat())
                )
            )
            labels.add(it.name)
        }
        val dataSet = BarDataSet(entries, "밥 준 사람")
        dataSet.colors = listOf(Color.GREEN, Color.YELLOW)
        val barData = BarData(dataSet)
        chart.data = barData
        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chart.xAxis.setCenterAxisLabels(true)
        chart.xAxis.granularity = 1F
        chart.xAxis.valueFormatter = MyXAxisFormatter()
        chart.legend.setCustom(
            listOf<LegendEntry>(
                LegendEntry(
                    "개",
                    Legend.LegendForm.DEFAULT,
                    Float.NaN,
                    Float.NaN,
                    null,
                    Color.GREEN
                ),
                LegendEntry(
                    "고양이",
                    Legend.LegendForm.DEFAULT,
                    Float.NaN,
                    Float.NaN,
                    null,
                    Color.YELLOW
                )
            )
        )
        chart.animateY(1400, Easing.EaseOutBounce)
        chart.invalidate()
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    inner class MyXAxisFormatter : ValueFormatter() {
        private val days = mutableListOf<String>()

        init {
            memberList.forEach {
                days.add(it.name)
            }
        }

        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return days.getOrNull(value.toInt()) ?: value.toString()
        }
    }
}

class Member(val name: String, val dogFood: Int, val catFood: Int) {
}