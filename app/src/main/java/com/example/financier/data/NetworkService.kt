package com.example.financier.data

import com.example.financier.data.model.AuthResponse
import com.example.financier.data.model.LoginRequest
import com.example.financier.data.model.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface NetworkService {
    @POST("/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>
}