package com.example.financier.data.model

import com.google.gson.annotations.SerializedName

data class AnalyticsRequest(
    @SerializedName("categories")
    val categories: List<String>? = null,
    @SerializedName("transactions")
    val operations: List<OperationResponse>? = null
)