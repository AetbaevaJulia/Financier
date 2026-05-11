package com.example.financier.data.model

data class RegisterRequest(
    val email: String,
    val password: String,
    val fullName: String? = null
)