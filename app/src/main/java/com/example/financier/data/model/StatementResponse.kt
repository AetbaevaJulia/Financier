package com.example.financier.data.model

import com.example.financier.enums.StatementStatus
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.util.UUID

@Serializable
data class StatementResponse(
    val id: UUID,
    val userId: UUID,
    val filename: String,
    val filePath: String? = null,
    val bank: String = "sber",
    val status: StatementStatus,
    val errorCode: String? = null,
    val errorMessage: String? = null,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

