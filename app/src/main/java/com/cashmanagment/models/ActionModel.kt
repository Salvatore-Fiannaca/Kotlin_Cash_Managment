package com.cashmanagment.models

data class ActionModel (
    val id: Int,
    val type: Int, // 0 in | 1 out
    val currency: String,
    val tag: String,
    val amount : Double
)
