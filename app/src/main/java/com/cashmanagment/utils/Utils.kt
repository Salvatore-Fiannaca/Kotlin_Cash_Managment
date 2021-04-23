package com.cashmanagment.utils

import android.util.Log

class Utils {

    fun cleanDoubleToString(counter: Double): String {
        var txt = "${counter.toFloat()}"
        val beforeDot = txt.substringBeforeLast(".")
        val afterDot = txt.substringAfterLast(".")

        if (afterDot.toInt() > 0) {
            txt = "$beforeDot.$afterDot"
        } else {
            txt = beforeDot
        }

        return txt
    }

}