package com.cashmanagment.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.iterator
import com.cashmanagment.R
import com.cashmanagment.database.DatabaseHandler
import com.cashmanagment.models.ActionModel
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.android.synthetic.main.activity_add.view.*

class AddActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        btnBack.setOnClickListener{
            onBackPressed()
        }
        btnAdd.setOnClickListener{
            if (checkInput()){
                val amount = input_amount.text.toString().toDouble()
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
        btnSub.setOnClickListener{
            if (checkInput()){
                val amount = input_amount.text.toString().toDouble()
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
        val amount = input_amount.text.toString()
        if (amount != "" && amount.toDouble() > 0)
            return true

        Toast.makeText(
            this,
            "PLEASE ENTER A NUMBER",
            Toast.LENGTH_SHORT).show()

        return false
    }

}
