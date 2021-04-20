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

        val items = getActions()

        var test1 = "3000.0"
        var test2 = "13000.0"

        var test3 = test1.substringBeforeLast(".")
        var test4 = test2.substringBeforeLast(".")

        if (test3.length >= 4){
            test3 = test3.substring(1)
        }

        Log.e("after 1", test3)
        Log.e("after 2", test4)

        layoutManager = LinearLayoutManager(this.requireContext())
        binding.recyclerView.layoutManager = layoutManager
        adapter = RecyclerAdapter(items)
        binding.recyclerView.adapter = adapter

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

}