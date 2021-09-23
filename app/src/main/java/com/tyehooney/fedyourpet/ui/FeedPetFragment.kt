package com.tyehooney.fedyourpet.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tyehooney.fedyourpet.databinding.FragmentFeedPetBinding
import com.tyehooney.fedyourpet.model.LogAdapter
import kotlin.random.Random

class FeedPetFragment : Fragment() {
    lateinit var binding : FragmentFeedPetBinding
    var logList = mutableListOf<String>()
    lateinit var adapter : LogAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFeedPetBinding.inflate(inflater, container, false)

        setListener()
        setAdapter()
        return binding.root
    }
    fun setListener(){
        binding.toolbar.setNavigationOnClickListener{
            Toast.makeText(activity, "back",Toast.LENGTH_SHORT).show()
        }

        binding.feedButton.setOnClickListener{
            var str = "나,"
            str += String.format("%02d",Random.nextInt(24))
            str += ":"
            str += String.format("%02d",Random.nextInt(60))
            str +=":"
            str += String.format("%02d",Random.nextInt(60))
            logList.add(str)
            adapter.notifyItemInserted(logList.lastIndex)
            binding.recyclerLog.scrollToPosition(logList.size - 1)
        }
    }
    fun setAdapter(){
        logList.add("아빠,09:17:35")
        logList.add("엄마,10:08:24")
        logList.add("딸,12:24:56")
        logList.add("아들,15:35:31")
        logList.add("할머니,16:23:33")

        adapter = LogAdapter(activity as Context)
        adapter.data = logList
        binding.recyclerLog.layoutManager = LinearLayoutManager(activity as Context, LinearLayoutManager.VERTICAL, false)
        val decoration = DividerItemDecoration(activity as Context, LinearLayoutManager.VERTICAL)
        binding.recyclerLog.addItemDecoration(decoration)
        binding.recyclerLog.adapter = adapter

    }

}