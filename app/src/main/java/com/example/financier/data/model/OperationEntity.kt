package com.example.financier.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.financier.enums.ClassificationSource
import com.example.financier.enums.OperationType
import java.sql.Time
import java.util.Date
import java.util.UUID

@Entity(
    tableName = OperationEntity.TABLE
)
data class OperationEntity (
    @PrimaryKey
    val operationId: String,
    val statementId: String,
    val operationDate: String,
    val postingDate: String? = null,
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
    val operationType: String,
    val balanceAfter: Double? = null,
    val classificationSource: String,
    val confidence: Double = 0.0
) {
    companion object {
        const val TABLE = "operations"
    }
}