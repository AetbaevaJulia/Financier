package com.example.financier.data.model

data class Transaction(
    val id: String,
    val operationDate: String,
    val amount: Double,
    val rawDescription: String,
    val merchant: String?,
    val category: String?,
    val operationType: String,
    val confidence: Double?
)