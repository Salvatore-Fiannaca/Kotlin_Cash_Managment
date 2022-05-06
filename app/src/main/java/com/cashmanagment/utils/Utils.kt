package com.cashmanagment.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Utils {

    fun cleanIntToString(counter: Int): String {
        var str = "$counter"
        if (str.contains("-"))
            return str
        if (str.length >3){

            val reverseString = str.reversed()
            val arrString = ArrayList<String>()

            var i = 1
            for (c in reverseString) {
                arrString.add(c.toString())

                val isMultipleOfThree = (i % 3) == 0
                val isNotLastChar = i < reverseString.length

                if (isMultipleOfThree && isNotLastChar) {
                    arrString.add(".")
                }
                i++
            }

            str = arrString.reversed().joinToString("")
        }

        return str
    }

    fun getTags(): ArrayList<String>{
        return  arrayListOf("Necessary", "Saving", "Investment", "Formation", "Fun", "Donation")
    }

    fun getCurrentDate(): String{
        val pattern = "yyyy-MM-dd"
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern(pattern)
        return current.format(formatter)
    }

}