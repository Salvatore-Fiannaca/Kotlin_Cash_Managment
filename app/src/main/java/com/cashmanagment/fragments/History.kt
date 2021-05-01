package com.cashmanagment.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cashmanagment.adapter.RecyclerAdapter
import com.cashmanagment.database.DatabaseHandler
import com.cashmanagment.databinding.FragmentHistoryBinding
import com.cashmanagment.models.HistoryModel

class History : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentHistoryBinding.inflate(layoutInflater)

        refreshValues()
    }

    override fun onResume() {
        super.onResume()
        refreshValues()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun getActions(): ArrayList<HistoryModel> {
        val dbHandler = DatabaseHandler(this.requireContext())
        return dbHandler.readAll()
    }

    private fun refreshValues(){
        layoutManager = LinearLayoutManager(this.requireContext())
        binding.recyclerView.layoutManager = layoutManager
        adapter = RecyclerAdapter(getActions())
        binding.recyclerView.adapter = adapter
    }

}