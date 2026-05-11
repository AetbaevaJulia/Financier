package com.example.financier.data.model

import com.example.financier.enums.StatementStatus
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class UploadStatementResponse(
    val statementId: UUID,
    val status: StatementStatus
)