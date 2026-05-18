package com.example.financier.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = OperationEntity.TABLE,
    indices = [Index(value = ["dateTime", "authorizationCode"], unique = true)]
)
data class OperationEntity (
    @PrimaryKey
    val operationId: String,
    val statementId: String,
    val dateTime: Long,
    val authorizationCode: String? = null,
    val amount: Double,
    val currency: String = "RUB",
    val rawDescription: String,
    val normalizedDescription: String,
    val bankCategory: String? = null,
    val merchant: String? = null,
    val category: String = "other",
    val subcategory: String? = null,
    val operationType: String,
    val balanceAfter: Double? = null,
    val classificationSource: String,
    val confidence: Double = 0.0
) {
    companion object {
        const val TABLE = "operations"
    }
}