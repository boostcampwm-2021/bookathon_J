package com.tyehooney.fedyourpet.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tyehooney.fedyourpet.databinding.TimelistItemBinding

class TimeListApapter : ListAdapter<Pair<Int, Int>, TimeListApapter.TimeListViewHolder>(diffUtil) {
    inner class TimeListViewHolder(binding: TimelistItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val hourOfDay = binding.hourOfDay
        val minute = binding.minute
        fun bind(pair: Pair<Int, Int>) {
            hourOfDay.text = pair.first.toString()
            minute.text = pair.second.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeListViewHolder {
        val binding =
            TimelistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TimeListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TimeListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Pair<Int, Int>>() {
            override fun areItemsTheSame(
                oldItem: Pair<Int, Int>,
                newItem: Pair<Int, Int>
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: Pair<Int, Int>,
                newItem: Pair<Int, Int>
            ): Boolean =
                oldItem == newItem
        }
    }
}