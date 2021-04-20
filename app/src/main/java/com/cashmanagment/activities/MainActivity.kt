package com.cashmanagment.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.*
import com.cashmanagment.R
import com.cashmanagment.database.DatabaseHandler
import com.cashmanagment.databinding.ActivityMainBinding
import com.cashmanagment.fragments.Dashboard
import com.cashmanagment.fragments.History
import com.cashmanagment.models.HistoryModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // FRAGMENTS
    private val dashboard = Dashboard()
    private val history = History()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupLayout()
    }
    override fun onResume() {
        super.onResume()
        readAllFromLocalDB()
    }

    // UTILS FUNCTIONS
    private fun setupLayout(){
        // SET FRAGMENT
        changeFragment("dashboard")

        // ADJUST NAVBAR
        binding.bottomNavigationView.background = null
        binding.bottomNavigationView.menu.getItem(2).isEnabled = false
        binding.bottomNavigationView.menu.getItem(1).setChecked(true)

        // EVENT CLICK HANDLER
        binding.fab.setOnClickListener{
            changeFragment("add")
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }
        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.btnDashboard -> {
                    changeFragment("dashboard")
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.btnHistory -> {
                    changeFragment("history")
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }

    }
    private fun changeFragment(frag : String){
        when(frag){
            "dashboard" -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, dashboard).commit()
            }
            "history" -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, history)
                        .commit()
            }
        }
    }

    // DATABASE HELPER
    private fun readAllFromLocalDB(): ArrayList<HistoryModel>{
        val dbHandler = DatabaseHandler(this)
        return dbHandler.readAll()
    }

}