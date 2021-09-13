package com.cashmanagment.activities

import android.os.Bundle
import com.cashmanagment.utils.Utils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.iterator
import com.cashmanagment.database.DatabaseHandler
import com.cashmanagment.databinding.ActivityAddBinding
import com.cashmanagment.models.HistoryModel

class AddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBinding
    private val utils = Utils()

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

        binding.inputAmount.requestFocus()
    }

    private fun submit(add: Boolean){
        var description = binding.inputDescription.text.toString()
        var amount = binding.inputAmount.text.toString().toInt()
        var tag = ""; var type = "in"

        if (!add){
            amount = -amount
            type = "out"
            tag = getTagChecked()
        }

        val item = HistoryModel(0, type, description, tag, amount)
        val dbHandler = DatabaseHandler(this)
        dbHandler.insertData(item)
        finish()
    }
    private fun checkInput(): Boolean {
        val amount = binding.inputAmount.text.toString()
        if (amount != "" && amount.toDouble() > 0)
            return true

        Toast.makeText(
                this,
                "PLEASE ENTER A NUMBER",
                Toast.LENGTH_SHORT).show()

        return false
    }
    private fun getTagChecked(): String {
        var tags = utils.getTags()
        val chipGroup = binding.chipGroup
        val chipChecked = chipGroup.checkedChipId

        var i = 0
        var tag = tags[i]
        for (chip in chipGroup){
            if (chipChecked == chip.id){
                tag = tags[i]
            }
            i++
        }
        return tag
    }

}
