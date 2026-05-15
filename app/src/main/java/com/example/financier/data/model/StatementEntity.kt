package com.example.financier.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.financier.enums.StatementStatus
import java.sql.Timestamp

@Entity(
    tableName = StatementEntity.TABLE
)
data class StatementEntity(
    @PrimaryKey
    val statementId: String,
    val userId: String,
    val filename: String,
    val filePath: String? = null,
    val bank: String = "sber",
    val status: String,
    val errorCode: String? = null,
    val errorMessage: String? = null,
    val createdAt: String,
    val updatedAt: String
) {
    companion object {
        const val TABLE = "statements"
    }
}