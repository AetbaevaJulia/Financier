package com.example.financier.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = OperationEntity.TABLE
)
data class OperationEntity (
    @PrimaryKey(autoGenerate = true)
    val operationId: Int = 0,
    val dateTime: Long,
    val authCode: Int,
    val category: String,
    val description: String,
    val amount: Float
) {
    companion object {
        const val TABLE = "operations"
    }
}