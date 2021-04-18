package com.cashmanagment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cashmanagment.R

class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>(){

    private var amount = arrayOf("100", "20", "50")
    private var tags = arrayOf("all", "all", "all")
    private var types = arrayOf("+", "-", "+")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return ViewHolder(v)

    }

    override fun getItemCount(): Int {
        return amount.size
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
        holder.itemAmount.text = amount[position]
        holder.itemType.text = types[position]
        holder.itemTag.text = tags[position]
    }

    inner class  ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var itemAmount: TextView = itemView.findViewById(R.id.item_amount)
        var itemTag: TextView = itemView.findViewById(R.id.item_tag)
        var itemType: TextView = itemView.findViewById(R.id.item_type)

    }

}

