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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.tyehooney.fedyourpet.databinding.FragmentFeedPetBinding
import com.tyehooney.fedyourpet.model.FeedLog
import com.tyehooney.fedyourpet.model.LogAdapter
import com.tyehooney.fedyourpet.model.Logs
import com.tyehooney.fedyourpet.util.addFeedLog
import com.tyehooney.fedyourpet.util.getTodaysLog
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

class FeedPetFragment : Fragment(), FeedPetListener {
    private lateinit var binding: FragmentFeedPetBinding
    private lateinit var adapter: LogAdapter
    private lateinit var profile: String // shared preference 에서 받아온 가족 구성원 프로필

    private var logList = mutableListOf<FeedLog>()
    private lateinit var petId: String
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFeedPetBinding.inflate(inflater, container, false)

        initialize()
        setListener()

        return binding.root
    }

    @SuppressLint("SimpleDateFormat")
    private fun initialize() {

        val args: FeedPetFragmentArgs by navArgs()
        petId = args.petId

        val datePattern = "yyyy-MM-dd"
        val simpleDateFormat = SimpleDateFormat(datePattern)
        val date = simpleDateFormat.format(Date())
        sharedPreferences = requireActivity().getSharedPreferences(
            "userInfo",
            Context.MODE_PRIVATE
        )

        profile = sharedPreferences.getString("profile", null).toString()
        binding.textPetName.setText(args.petName)
        binding.textDate.text = date
        Glide.with(activity as Context).load(args.petImage).into(binding.imagePet)

        getTodaysLog(petId, this)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setListener() {
        binding.imageBackButton.setOnClickListener{
            findNavController().navigateUp()
            Toast.makeText(activity, "back", Toast.LENGTH_SHORT).show()
        }
        binding.feedButton.setOnClickListener {
            val uid = sharedPreferences.getString("uid", null)
            uid?.let { addFeedLog(petId, uid, profile, this) }
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
        binding.recyclerLog.scrollToPosition(adapter.data.size - 1)
    }

    override fun onGetLogsSuccess(logs: List<FeedLog>) {
        logList.clear()
        logList.addAll(logs)
        setAdapter()
    }

    override fun onGetLogsFailed(msg: String) {
        Toast.makeText(context, "error : $msg", Toast.LENGTH_SHORT).show()
    }

    override fun onAddLogSuccess() {
        getTodaysLog(petId, this)
    }

    override fun onAddLogFailed(msg: String) {
        Toast.makeText(context, "error : $msg", Toast.LENGTH_SHORT).show()
    }
}