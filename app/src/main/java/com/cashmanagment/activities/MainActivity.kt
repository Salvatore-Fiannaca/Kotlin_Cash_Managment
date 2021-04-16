package com.cashmanagment.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.*
import com.cashmanagment.R
import com.cashmanagment.database.DatabaseHandler
import com.cashmanagment.fragments.Dashboard
import com.cashmanagment.fragments.Settings
import com.cashmanagment.models.ActionModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_dashboard.*

class MainActivity : AppCompatActivity() {

    private var items = ArrayList<ActionModel>()
    private var count: Double = 0.0

    // FRAGMENTS
    private val dashboard = Dashboard()
    private val settings = Settings()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupLayout()

        val count = getCashAvailable()
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
        bottomNavigationView.background = null
        bottomNavigationView.menu.getItem(1).isEnabled = false
        bottomNavigationView.menu.getItem(2).setChecked(true)

        // EVENT CLICK HANDLER
        fab.setOnClickListener{
            changeFragment("add")
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.btnDashboard -> {
                    changeFragment("dashboard")
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.btnSettings -> {
                    changeFragment("settings")
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
            "settings" -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, settings)
                        .commit()
            }
        }
    }

    // DB HELPER FUNCTIONS
    private fun readAllFromLocalDB(): ArrayList<ActionModel>{
        val dbHandler = DatabaseHandler(this)
        return dbHandler.readAll()
    }
    private fun getCashAvailable(): Double {
        val dbHandler = DatabaseHandler(this)
        return dbHandler.getCounter()
    }


}