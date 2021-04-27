package com.cashmanagment.adapter

import android.graphics.Color
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
        val currentType = items[position].type
        when (currentType){
            "in"-> holder.itemType.setTextColor(Color.GREEN)
            "out"-> holder.itemType.setTextColor(Color.RED)
        }

        holder.itemType.text = currentType
        holder.itemAmount.text = items[position].amount.toString()
        holder.itemDescription.text = items[position].description
        holder.itemTag.text = items[position].tag
    }

    inner class  ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val itemAmount: TextView = itemView.findViewById(R.id.item_amount)
        val itemTag: TextView = itemView.findViewById(R.id.item_tag)
        val itemType: TextView = itemView.findViewById(R.id.item_type)
        val itemDescription: TextView = itemView.findViewById(R.id.item_description)

    }

}

