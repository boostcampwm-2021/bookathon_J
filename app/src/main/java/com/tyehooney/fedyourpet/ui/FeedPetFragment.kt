package com.tyehooney.fedyourpet.ui

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager

import com.bumptech.glide.Glide

import com.google.firebase.firestore.FirebaseFirestore
import com.tyehooney.fedyourpet.databinding.FragmentFeedPetBinding
import com.tyehooney.fedyourpet.model.LogAdapter
import com.tyehooney.fedyourpet.model.Logs
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import kotlin.random.Random

class FeedPetFragment : Fragment() {
    lateinit var binding : FragmentFeedPetBinding
    var logList = mutableListOf<String>()
    lateinit var adapter : LogAdapter
    var fbFirestore : FirebaseFirestore? = null

    var cnt = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFeedPetBinding.inflate(inflater, container, false)

        initialize()
        setListener()
        setAdapter()

        return binding.root
    }

    fun initialize(){
        var datePattern = "yyyy-MM-dd"
        var simpleDateFormat = SimpleDateFormat(datePattern)
        var date = simpleDateFormat.format(Date())

        binding.textDate.setText("${date}")

        var img = "https://firebasestorage.googleapis.com/v0/b/fedyourpet-aa497.appspot.com/o/%EB%A9%8D%EB%AD%89%EC%9D%B4_1632426381729.png?alt=media&token=a96fab84-b07b-4f02-88f9-a411875102bb"
        Glide.with(activity as Context)
            .load(img)
            .into(binding.imagePet)

        fbFirestore = FirebaseFirestore.getInstance()
        for (i in 0..100){
            fbFirestore?.collection("Logs_Dog")?.document("${i}회")?.delete()
        }
    }
    fun setListener(){
        binding.toolbar.setNavigationOnClickListener{
            Toast.makeText(activity, "back",Toast.LENGTH_SHORT).show()
        }

        binding.feedButton.setOnClickListener{
            var str = ""
            var type = Random.nextInt(5)
            when (type) {
                0 -> {str = "할머니"}
                1 -> {str = "엄마"}
                2 -> {str = "아빠"}
                3 -> {str = "아들"}
                4 -> {str = "딸"}
            }
            var time = ""
            time += String.format("%02d",Random.nextInt(24))
            time += ":"
            time += String.format("%02d",Random.nextInt(60))
            time +=":"
            time += String.format("%02d",Random.nextInt(60))
            fbFirestore?.collection("Logs_Dog")?.document("${cnt}회")?.set(mapOf("whoWhen" to ("${str},${time}")))
            cnt++

            fbFirestore?.collection("Logs_Dog")?.addSnapshotListener{
                    snapshot, exception ->
                logList.clear()
                for (shot in snapshot!!.documents) {
                    logList.add(shot.toObject(Logs::class.java)!!.whoWhen)
                }
                adapter.data = logList
                adapter.notifyDataSetChanged()
                binding.recyclerLog.scrollToPosition(logList.size - 1)
            }
        }


    }
    fun setAdapter(){

        adapter = LogAdapter(activity as Context)
        adapter.data = logList
        binding.recyclerLog.layoutManager = LinearLayoutManager(activity as Context, LinearLayoutManager.VERTICAL, false)
        val decoration = DividerItemDecoration(activity as Context, LinearLayoutManager.VERTICAL)
        binding.recyclerLog.addItemDecoration(decoration)
        binding.recyclerLog.adapter = adapter

    }

}