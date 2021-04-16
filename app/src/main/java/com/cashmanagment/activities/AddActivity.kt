package com.cashmanagment.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.cashmanagment.database.DatabaseHandler
import com.cashmanagment.databinding.ActivityAddBinding
import com.cashmanagment.models.ActionModel

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
                val amount = binding.inputAmount.text.toString().toDouble()
                // TODO: TIMESTAMP
                val values = ActionModel(
                    0,
                    0,
                    "euro",
                    "all",
                    amount,
                )
                submit(values)
            }
        }
        binding.btnSub.setOnClickListener{
            if (checkInput()){
                val amount = binding.inputAmount.text.toString().toDouble()
                val values = ActionModel(
                    0,
                    1,
                    "euro",
                    "all",
                    -amount
                )
                submit(values)
            }
        }
    }

    private fun submit(actions: ActionModel){
        val item = ActionModel(
            actions.id,
            actions.type,
            actions.currency,
            actions.tag,
            actions.amount,
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
