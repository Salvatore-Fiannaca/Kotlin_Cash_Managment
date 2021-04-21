package com.cashmanagment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cashmanagment.R
import com.cashmanagment.models.HistoryModel

class RecyclerAdapter(private val items: ArrayList<HistoryModel>) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return ViewHolder(v)

    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
        holder.itemAmount.text = items[position].amount.toString()
        holder.itemType.text = items[position].type
        holder.itemDescription.text = items[position].description
        holder.itemTag.text = items[position].tag
    }

    inner class  ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var itemAmount: TextView = itemView.findViewById(R.id.item_amount)
        var itemTag: TextView = itemView.findViewById(R.id.item_tag)
        var itemType: TextView = itemView.findViewById(R.id.item_type)
        var itemDescription: TextView = itemView.findViewById(R.id.item_description)

    }

}

