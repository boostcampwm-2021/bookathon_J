package com.tyehooney.fedyourpet.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.tyehooney.fedyourpet.databinding.FragmentFeedPetBinding
import com.tyehooney.fedyourpet.model.LogAdapter
import com.tyehooney.fedyourpet.model.Logs
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

class FeedPetFragment : Fragment() {
    private lateinit var binding: FragmentFeedPetBinding
    private lateinit var adapter: LogAdapter
    private lateinit var profile: String // shared preference 에서 받아온 가족 구성원 프로필

    private var logList = mutableListOf<String>()
    private var fbFirestore: FirebaseFirestore? = null
    private var cnt = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFeedPetBinding.inflate(inflater, container, false)

        initialize()
        setListener()
        setAdapter()

        return binding.root
    }

    @SuppressLint("SimpleDateFormat")
    private fun initialize() {
        val datePattern = "yyyy-MM-dd"
        val simpleDateFormat = SimpleDateFormat(datePattern)
        val date = simpleDateFormat.format(Date())
        val img =
            "https://firebasestorage.googleapis.com/v0/b/fedyourpet-aa497.appspot.com/o/%EB%A9%8D%EB%AD%89%EC%9D%B4_1632426381729.png?alt=media&token=a96fab84-b07b-4f02-88f9-a411875102bb"
        val sharedPreferences = requireActivity().getSharedPreferences(
            "userInfo",
            Context.MODE_PRIVATE
        )

        profile = sharedPreferences.getString("profile", null).toString()
        binding.textDate.text = date
        Glide.with(activity as Context).load(img).into(binding.imagePet)
        fbFirestore = FirebaseFirestore.getInstance()

        // FIXME
        for (i in 0..100) {
            fbFirestore?.collection("Logs_Dog")
                ?.document("${i}회")
                ?.delete()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setListener() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
            Toast.makeText(activity, "back", Toast.LENGTH_SHORT).show()
        }

        binding.feedButton.setOnClickListener {
            // TODO: DB 연동
            var time = ""
            time += String.format("%02d", Random.nextInt(24))
            time += ":"
            time += String.format("%02d", Random.nextInt(60))
            time += ":"
            time += String.format("%02d", Random.nextInt(60))

            fbFirestore?.collection("Logs_Dog")
                ?.document("${cnt}회")
                ?.set(mapOf("whoWhen" to ("${profile},${time}")))

            cnt++

            fbFirestore?.collection("Logs_Dog")
                ?.addSnapshotListener { snapshot, exception ->
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

    private fun setAdapter() {
        adapter = LogAdapter(activity as Context)
        adapter.data = logList
        binding.recyclerLog.layoutManager =
            LinearLayoutManager(activity as Context, LinearLayoutManager.VERTICAL, false)

        val decoration = DividerItemDecoration(activity as Context, LinearLayoutManager.VERTICAL)
        binding.recyclerLog.addItemDecoration(decoration)
        binding.recyclerLog.adapter = adapter
    }
}