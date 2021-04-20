package com.cashmanagment.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.cashmanagment.database.DatabaseHandler
import com.cashmanagment.databinding.ActivityAddBinding
import com.cashmanagment.models.HistoryModel

class AddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener{
            onBackPressed()
        }
        binding.btnAdd.setOnClickListener{
            if (checkInput()){
                submit(true)
            }
        }
        binding.btnSub.setOnClickListener{
            if (checkInput()){
                submit(false)
            }
        }
    }

    private fun submit(add: Boolean){
        var amount = binding.inputAmount.text.toString().toDouble()
        var type = "in"

        if (!add){
            amount = -amount
            type = "out"
        }

        val item = HistoryModel(
                0,
                type,
                "euro",
                "all",
                amount
        )
        val dbHandler = DatabaseHandler(this)
        dbHandler.insertData(item)
        finish()
    }
    private fun checkInput(): Boolean{
        val amount = binding.inputAmount.text.toString()
        if (amount != "" && amount.toDouble() > 0)
            return true

        Toast.makeText(
            this,
            "PLEASE ENTER A NUMBER",
            Toast.LENGTH_SHORT).show()

        return false
    }

}
