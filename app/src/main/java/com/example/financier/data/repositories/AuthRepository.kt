package com.example.financier.data.repository

import android.util.Log
import com.example.financier.data.NetworkService
import com.example.financier.data.model.AuthResponse
import com.example.financier.data.model.LoginRequest
import com.example.financier.data.model.RegisterRequest
import javax.inject.Inject

interface AuthRepository {
    suspend fun register(email: String, password: String): Result<AuthResponse>
    suspend fun login(email: String, password: String): Result<AuthResponse>
}

class AuthRepositoryImpl @Inject constructor(
    private val authApi: NetworkService
) : AuthRepository {

    override suspend fun register(email: String, password: String): Result<AuthResponse> {
        return try {
            val response = authApi.register(RegisterRequest(email, password))
            if (response.isSuccessful && response.body() != null) {
                Log.d("logreg", response.body().toString())
                Result.success(response.body()!!)
            } else {
                Log.d("Ошибка", response.toString())
                Result.failure(Exception("Registration failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.d("Ошибка в кетч", e.message.toString())
            Result.failure(e)
        }
    }

    override suspend fun login(email: String, password: String): Result<AuthResponse> {
        return try {
            val response = authApi.login(LoginRequest(email, password))
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Login failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}