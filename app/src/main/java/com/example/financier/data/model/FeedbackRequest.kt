package com.example.financier.data.model

import com.example.financier.enums.OperationType
import com.google.gson.annotations.SerializedName

data class FeedbackRequest (
    @SerializedName("merchant")
    val merchant: String? = null,
    @SerializedName("category")
    val category: String? = null,
    @SerializedName("subcategory")
    val subcategory: String? = null,
    @SerializedName("operation_type")
    val operationType: OperationType? = null
)