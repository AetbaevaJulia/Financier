package com.example.financier.data.model

import com.example.financier.enums.ClassificationSource
import com.example.financier.enums.OperationType
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.util.UUID

@Serializable
data class OperationResponse(
    val id: UUID,
    val statementId: UUID,
    val operationDate: LocalDate,
    val postingDate: LocalDate? = null,
    val time: String? = null,
    val authorizationCode: String? = null,
    val amount: Double,
    val currency: String = "RUB",
    val rawDescription: String,
    val normalizedDescription: String,
    val bankCategory: String? = null,
    val merchant: String? = null,
    val category: String = "other",
    val subcategory: String? = null,
    val operationType: OperationType,
    val balanceAfter: Double? = null,
    val classificationSource: ClassificationSource,
    val confidence: Double = 0.0
)