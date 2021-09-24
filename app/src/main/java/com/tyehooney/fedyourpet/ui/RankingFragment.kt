package com.tyehooney.fedyourpet.ui

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
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
import com.tyehooney.fedyourpet.model.FeedLog
import com.tyehooney.fedyourpet.model.Pet
import com.tyehooney.fedyourpet.util.getAllLog
import com.tyehooney.fedyourpet.util.getMyPets
import com.tyehooney.fedyourpet.util.getProfiles

class RankingFragment : Fragment(R.layout.fragment_ranking), RankingListener {
    private var _binding: FragmentRankingBinding? = null
    private val binding get() = _binding!!
    private val memberMap = mutableMapOf<String, Member>()
    private val entries = arrayListOf<BarEntry>()
    val labels = mutableSetOf<String>()
    lateinit var chart: BarChart
    private val pets = mutableListOf<Pet>()
    private lateinit var sharedPreferences: SharedPreferences
    private val colors = listOf(Color.GREEN, Color.YELLOW, Color.BLUE, Color.CYAN)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRankingBinding.inflate(inflater, container, false)
        val view = binding.root
        chart = binding.rankingBarchart
        sharedPreferences = requireActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE)
        getProfsAndPets()
        Log.d("entry", entries.toString())
        return view
    }

    private fun getProfsAndPets() {
        val uid = sharedPreferences.getString("uid", null)
        uid?.let { userId ->
            getProfiles(userId, this)
            getMyPets(userId, this)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onProfileReceived(profiles: List<String>) {
        profiles.forEach {
            memberMap[it] = Member(it)
        }
    }

    override fun onGetMyPetsSuccess(pets: List<Pet>) {
        this.pets.addAll(pets)
        Log.d("pets", this.pets.toString())
        this.pets.forEach {
            memberMap.values.forEach { member ->
                member.feedNums[it.name] = 0

            }
            getAllLog(it.name, it.id, this)
        }

    }

    private fun drawChart() {
        val dataSet = BarDataSet(entries, "밥 준 사람")
        dataSet.colors = colors
        val barData = BarData(dataSet)
        chart.data = barData
        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chart.xAxis.setCenterAxisLabels(true)
        chart.xAxis.setAvoidFirstLastClipping(true)
        chart.xAxis.granularity = 1F
        chart.xAxis.setLabelCount(labels.size, true)
        chart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        chart.xAxis.axisMinimum = 0F
        val legendEntry = mutableListOf<LegendEntry>()
        pets.forEachIndexed { idx, it ->
            legendEntry.add(LegendEntry(it.name, Legend.LegendForm.DEFAULT,
                Float.NaN,
                Float.NaN,
                null, colors[idx % colors.size]))
        }
        chart.legend.setCustom(legendEntry)
        chart.animateY(1400, Easing.EaseOutBounce)
        chart.invalidate()
    }

    override fun onGetMyPetsFailed(msg: String) {
        Toast.makeText(context, "error : $msg", Toast.LENGTH_SHORT).show()
    }

    override fun onGetLogsSuccess(logs: List<FeedLog>, petName: String) {
        if (logs.isNotEmpty()) {
            logs.forEach {
                when (val count = memberMap[it.who]?.feedNums?.get(petName)) {
                    null -> memberMap[it.who]?.feedNums?.set(petName, 1)
                    else -> memberMap[it.who]?.feedNums?.set(petName, count + 1)
                }
            }
            Log.d("logs", memberMap.toString())
        }
        memberMap.values.forEachIndexed { idx, member ->
            entries.add(
                BarEntry(
                    idx.toFloat() + 0.5F,
                    member.feedNums.toList().map { floatMap -> floatMap.second.toFloat() }
                        .toFloatArray()
                )
            )
            labels.add(member.name)
            Log.d("label", labels.toString())
        }
        Log.d("entry", entries.toString())
        drawChart()
    }


    override fun onGetLogsFailed(msg: String) {
        Toast.makeText(context, "error: $msg", Toast.LENGTH_SHORT).show()
    }
}

class Member(val name: String) {
    val feedNums = linkedMapOf<String, Int>()
    override fun toString(): String {
        return "$name: $feedNums"
    }
}