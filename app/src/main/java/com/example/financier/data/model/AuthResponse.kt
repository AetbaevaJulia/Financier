package com.example.financier.data.model

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("user_id")
    val user_id: String,
    @SerializedName("token")
    val token: String
)