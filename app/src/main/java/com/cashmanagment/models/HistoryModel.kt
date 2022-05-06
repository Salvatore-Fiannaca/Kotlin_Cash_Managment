package com.cashmanagment.models

data class HistoryModel (
    val id: Int,
    val timestamp: String,
    val type: String,
    val tag: String,
    val amount : Int
)
