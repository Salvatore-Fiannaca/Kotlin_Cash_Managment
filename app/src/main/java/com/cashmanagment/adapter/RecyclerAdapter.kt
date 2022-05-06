package com.cashmanagment.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cashmanagment.R
import com.cashmanagment.database.DatabaseHandler
import com.cashmanagment.models.HistoryModel
import com.cashmanagment.utils.Utils
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlin.math.abs

class RecyclerAdapter(private val items: ArrayList<HistoryModel>) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return ViewHolder(v)

    }

    override fun getItemCount(): Int {
        return items.size
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
        val ctx = holder.itemDelete.context
        val id = items[position].id.toString()
        val amount = items[position].amount
        val tag = items[position].tag

        var timestamp = items[position].timestamp
        //if (timestamp == Utils().getCurrentDate()) timestamp = "Today"

        when (items[position].type){
          "in"-> {
              holder.itemAmount.setTextColor(Color.GREEN)
              holder.itemAmount.text = "+$amount"
          }
          "out"-> {
              holder.itemAmount.text = "$amount"
              holder.itemAmount.setTextColor(Color.RED)
          }
        }
        holder.itemTimestamp.text = timestamp
        holder.itemTag.text = tag

        holder.itemDelete.setOnClickListener{
            MaterialAlertDialogBuilder(ctx)
                    .setMessage("Are you sure to delete this?")
                    .setNegativeButton("Decline") { dialog, _ ->
                        dialog.cancel()
                    }
                    .setPositiveButton("Confirm") { dialog, _ ->
                        deleteItem(id, amount, ctx)
                        items.remove(items[position])

                        notifyDataSetChanged()
                        dialog.cancel()
                    }
                    .show()
        }
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val itemAmount: TextView = itemView.findViewById(R.id.item_amount)
        val itemTag: TextView = itemView.findViewById(R.id.item_tag)
        val itemTimestamp: TextView = itemView.findViewById(R.id.item_timestamp)
        val itemDelete: TextView = itemView.findViewById(R.id.item_delete)
    }

    private fun deleteItem(id: String, amount: Int, ctx: Context){
        val dbHandler = DatabaseHandler(ctx)
        dbHandler.deleteData(id)

        var n = amount
        if (n < 0) n = abs(n)
        else if (n > 0) n = -n

        dbHandler.updateCounter(n)
    }
}



