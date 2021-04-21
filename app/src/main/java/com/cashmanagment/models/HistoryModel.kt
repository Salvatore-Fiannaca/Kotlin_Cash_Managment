package com.cashmanagment.models

data class HistoryModel (
    val id: Int,
    val type: String,
    val description: String,
    val tag: String,
    val amount : Double
)
