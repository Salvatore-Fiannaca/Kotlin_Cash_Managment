package com.cashmanagment.utils

import android.util.Log
import kotlin.math.roundToInt
import kotlin.math.roundToLong

class Utils {

    fun cleanDoubleToString(counter: Double): String {
        var txt = "${counter.toFloat()}"
        var beforeDot = txt.substringBeforeLast(".")
        val afterDot = txt.substringAfterLast(".")

        if (beforeDot.length >3){
            var reverseString = beforeDot.reversed()
            var arrString = ArrayList<String>();
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

            val fixedString = arrString.reversed().joinToString("")
            beforeDot = fixedString
        }

        if (afterDot.toDouble() > 0) {
            txt = "$beforeDot.$afterDot"
        } else {
            txt = beforeDot
        }

        return txt
    }

}