package com.example.financier.enums

import com.google.gson.annotations.SerializedName

enum class OperationType {
    @SerializedName("income")
    INCOME,
    @SerializedName("expense")
    EXPENSE,
    @SerializedName("transfer")
    TRANSFER,
    @SerializedName("refund")
    REFUND,
    @SerializedName("fee")
    FEE,
    @SerializedName("cash_withdrawal")
    CASH_WITHDRAWAL,
    @SerializedName("cashback")
    CASHBACK,
    @SerializedName("unknown")
    UNKNOWN
}