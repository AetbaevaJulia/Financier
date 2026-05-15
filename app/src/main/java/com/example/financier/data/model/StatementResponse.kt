package com.example.financier.data.model

import com.example.financier.enums.StatementStatus
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.util.UUID

data class StatementResponse(
    @SerializedName("id")
    val statementId: UUID,
    @SerializedName("user_id")
    val userId: UUID,
    @SerializedName("filename")
    val filename: String,
    @SerializedName("file_path")
    val filePath: String? = null,
    @SerializedName("bank")
    val bank: String = "sber",
    @SerializedName("status")
    val status: String,
    @SerializedName("error_code")
    val errorCode: String? = null,
    @SerializedName("error_message")
    val errorMessage: String? = null,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String
)