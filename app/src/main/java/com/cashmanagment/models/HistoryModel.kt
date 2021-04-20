package com.cashmanagment.models

data class HistoryModel (
    val id: Int,
    val type: String,
    val currency: String,
    val tag: String,
    val amount : Double
)
