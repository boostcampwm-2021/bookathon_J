package com.tyehooney.fedyourpet.ui

import android.app.Activity
import android.app.TimePickerDialog
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tyehooney.fedyourpet.R
import com.tyehooney.fedyourpet.databinding.FragmentAnimalAddBinding
import com.tyehooney.fedyourpet.util.addNewPet
import java.util.*


class AnimalAddFragment : Fragment(R.layout.fragment_animal_add), AnimalAddListener {
    private var _binding: FragmentAnimalAddBinding? = null
    private val binding get() = _binding!!
    private val timeList = mutableListOf<Pair<Int, Int>>()
    private val timeListLiveData: MutableLiveData<List<Pair<Int, Int>>> by lazy {
        MutableLiveData()
    }
    private var petBitmap: Bitmap? = null
    private lateinit var sharedPreferences: SharedPreferences
    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val imageURI = data?.data
                Glide.with(this).asBitmap().load(imageURI).override(300, 300).fitCenter()
                    .into(object : CustomTarget<Bitmap>() {
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap>?,
                        ) {
                            //resource: 불러온 이미지 비트맵
                            petBitmap = resource
                            binding.fragmentAnimalAddImage.setImageBitmap(resource)
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {
                        }
                    })
            }
        }

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
            fragmentAnimalAddImage.setOnClickListener {
                chooseImage()
            }
        }

        sharedPreferences = requireActivity().getSharedPreferences("userInfo", MODE_PRIVATE)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = TimeListApapter()
        val recyclerView = binding.fragmentAnimalAddTimeview
        timeListLiveData.observe(viewLifecycleOwner, {
            adapter.submitList(it.toList())
        })
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        binding.confirmAnimalAddButton.setOnClickListener {
            val name = binding.fragmentAnimalAddEdittextName.text.toString()
            val uid = sharedPreferences.getString("uid", null)
            val feedingTimes = timeList.map {
                "${it.first}:${it.second}"
            }
            if (name.isNotEmpty() && uid != null && feedingTimes.isNotEmpty()) {
                petBitmap?.let {
                    addNewPet(uid, name, feedingTimes, it, this)
                    goBack()
                }
            }
        }

        binding.cancelAnimalAddButton.setOnClickListener {
            goBack()
        }
    }

    private fun goBack() {
        findNavController().navigate(R.id.action_animalAddFragment_to_mainFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun addTime() {
        val hourOfDay = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val minute = Calendar.getInstance().get(Calendar.MINUTE)
        val timePickerDialog =
            TimePickerDialog(context, TimePickerDialog.OnTimeSetListener { timePicker, i, i2 ->
                timeList.add(Pair(i, i2))
                timeListLiveData.value = timeList
            }, hourOfDay, minute, true)
        timePickerDialog.setMessage("밥 주는 시간")
        timePickerDialog.show()
    }

    private fun chooseImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        resultLauncher.launch(intent)
    }

    override fun onNewPetAdded() {
        Toast.makeText(context, "새 애완동물이 추가되었습니다!", Toast.LENGTH_SHORT).show()
        goBack()
    }

    override fun onAddNewPetFailed(msg: String) {
        Toast.makeText(context, "error! : $msg", Toast.LENGTH_SHORT).show()
    }
}