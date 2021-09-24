package com.tyehooney.fedyourpet.model

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tyehooney.fedyourpet.R
import java.text.SimpleDateFormat

class LogAdapter(val context: Context) : RecyclerView.Adapter<FeedLogViewHolder>() {
    lateinit var data: MutableList<FeedLog>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedLogViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_log, parent, false)
        return FeedLogViewHolder(view)
    }

    override fun onBindViewHolder(holderFeedLog: FeedLogViewHolder, position: Int) {
        val item = data.get(position)
        holderFeedLog.bind(item)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}

class FeedLogViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var tvWho : TextView = view.findViewById(R.id.text_who)
    var tvWhen : TextView = view.findViewById(R.id.text_when)

    fun bind(item: FeedLog){
        tvWho.text = item.who
        val dateFormat = SimpleDateFormat("HH시 mm분")
        tvWhen.text = dateFormat.format(item.feedAt)
    }
}
