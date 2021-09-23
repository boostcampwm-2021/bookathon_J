package com.tyehooney.fedyourpet

import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tyehooney.fedyourpet.databinding.FragmentAnimalAddBinding
import java.util.*


class AnimalAdd : Fragment(R.layout.fragment_animal_add) {
    private var _binding: FragmentAnimalAddBinding? = null
    private val binding get() = _binding!!
    private val timeList = mutableListOf<Pair<Int, Int>>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAnimalAddBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.apply {
            fragmentAnimalAddButtonTime.setOnClickListener {
                addTime()
                Log.d("times", timeList.toString())
            }
        }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun addTime() {
        val hourOfDay = Calendar.HOUR_OF_DAY
        val minute = Calendar.MINUTE
        val timePickerDialog =
            TimePickerDialog(context, TimePickerDialog.OnTimeSetListener { timePicker, i, i2 ->
                timeList.add(Pair(i, i2))
            }, hourOfDay, minute, true)
        timePickerDialog.setMessage("밥 주는 시간")
        timePickerDialog.show()
    }
}