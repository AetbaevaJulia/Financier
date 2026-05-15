package com.example.financier.data.model

import com.example.financier.enums.StatementStatus
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable
import java.util.UUID

data class UploadStatementResponse(
    @SerializedName("statement_id")
    val statementId: UUID,
    @SerializedName("status")
    val status: String
)