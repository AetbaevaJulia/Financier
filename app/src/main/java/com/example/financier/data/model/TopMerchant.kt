package com.example.financier.data.model

import com.google.gson.annotations.SerializedName

data class TopMerchant(
    @SerializedName("merchant")
    val merchant: String,
    @SerializedName("amount")
    val amount: Double
)