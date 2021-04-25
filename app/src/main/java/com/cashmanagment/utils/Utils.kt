package com.cashmanagment.utils

class Utils {

    fun cleanIntToString(counter: Int): String {
        var str = "$counter"

        if (str.length >3){
            var reverseString = str.reversed()
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

            str = arrString.reversed().joinToString("")
        }

        return str
    }

}