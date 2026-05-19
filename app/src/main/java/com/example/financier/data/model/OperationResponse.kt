package com.example.financier.data.model

import com.example.financier.enums.ClassificationSource
import com.google.gson.annotations.SerializedName
import java.util.UUID

data class OperationResponse(
    @SerializedName("id")
    val operationId: UUID,
    @SerializedName("statement_id")
    val statementId: UUID,
    @SerializedName("operation_date")
    val operationDate: String,
    @SerializedName("posting_date")
    val postingDate: String? = null,
    @SerializedName("time")
    val time: String? = null,
    @SerializedName("authorization_code")
    val authorizationCode: String? = null,
    @SerializedName("amount")
    val amount: Double,
    @SerializedName("currency")
    val currency: String = "RUB",
    @SerializedName("raw_description")
    val rawDescription: String,
    @SerializedName("normalized_description")
    var normalizedDescription: String,
    @SerializedName("bank_category")
    val bankCategory: String? = null,
    @SerializedName("merchant")
    val merchant: String? = null,
    @SerializedName("category")
    var category: String = "Прочее",
    @SerializedName("subcategory")
    var subcategory: String? = null,
    @SerializedName("operation_type")
    val operationType: String,
    @SerializedName("balance_after")
    val balanceAfter: Double? = null,
    @SerializedName("classification_source")
    val classificationSource: String,
    @SerializedName("confidence")
    val confidence: Double = 0.0
)