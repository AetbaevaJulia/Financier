package com.example.financier.enums

import kotlinx.serialization.Serializable

@Serializable
enum class OperationType {
    INCOME,
    EXPENSE,
    TRANSFER,
    REFUND,
    FEE,
    CASH_WITHDRAWAL,
    CASHBACK,
    UNKNOWN
}