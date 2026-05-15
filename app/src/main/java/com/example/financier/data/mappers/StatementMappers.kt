package com.example.financier.data.mappers

import com.example.financier.data.model.StatementEntity
import com.example.financier.data.model.StatementResponse
import java.sql.Timestamp
import java.util.UUID

fun StatementResponse.toEntity(): StatementEntity = StatementEntity(
    statementId = statementId.toString(),
    userId = userId.toString(),
    filename = filename,
    filePath = filePath,
    bank = bank,
    status = status,
    errorCode = errorCode,
    errorMessage = errorMessage,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun StatementEntity.toResponse(): StatementResponse = StatementResponse(
    statementId = UUID.fromString(statementId),
    userId = UUID.fromString(userId),
    filename = filename,
    filePath = filePath,
    bank = bank,
    status = status,
    errorCode = errorCode,
    errorMessage = errorMessage,
    createdAt = createdAt,
    updatedAt = updatedAt
)