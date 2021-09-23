package com.tyehooney.fedyourpet.model

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tyehooney.fedyourpet.R

class LogAdapter(val context: Context) : RecyclerView.Adapter<ViewHolder>() {
    lateinit var data: MutableList<String>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_log, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data.get(position)
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var tvWho : TextView = view.findViewById(R.id.text_who)
    var tvWhen : TextView = view.findViewById(R.id.text_when)

    fun bind(item: String){
        var ind = -1
        var hour = 0;
        var min = 0
        var sec = 0
        for(i in 0..item.length - 1) {
            if (item[i] == ','){
                ind = i
                break
            }
        }
        tvWho.setText(item.substring(0, ind))
        ind++
        hour = item.substring(ind, ind+ 2).toInt()
        ind += 3
        min = item.substring(ind, ind + 2).toInt()
        ind += 3
        sec = item.substring(ind, ind + 2).toInt()
        tvWhen.setText("${hour}시${min}분${sec}초")
    }

}
