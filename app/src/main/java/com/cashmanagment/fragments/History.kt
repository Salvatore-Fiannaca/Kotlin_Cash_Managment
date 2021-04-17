package com.cashmanagment.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cashmanagment.R
import com.cashmanagment.adapter.RecyclerAdapter
import com.cashmanagment.database.DatabaseHandler
import com.cashmanagment.databinding.FragmentDashboardBinding
import com.cashmanagment.databinding.FragmentHistoryBinding
import com.cashmanagment.models.ActionModel

class History : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentHistoryBinding.inflate(layoutInflater)

        val items = getActions()
        for (i in items){
            Log.e("item id:", i.id.toString())
            Log.e("item tag", i.tag)
            Log.e("item type", i.type.toString())
            Log.e("item amount", i.amount.toString())

        }


        layoutManager = LinearLayoutManager(this.requireContext())
        binding.recyclerView.layoutManager = layoutManager
        adapter = RecyclerAdapter()
        binding.recyclerView.adapter = adapter

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun getActions(): ArrayList<ActionModel> {
        val dbHandler = DatabaseHandler(this.requireContext())
        return dbHandler.readAll()
    }

}