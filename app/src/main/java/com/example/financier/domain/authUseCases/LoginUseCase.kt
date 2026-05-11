package com.example.financier.domain.authUseCases

import com.example.financier.data.model.AuthResponse
import com.example.financier.data.repository.AuthRepository
import javax.inject.Inject

interface LoginUseCase {
    suspend operator fun invoke(email: String, password: String): Result<AuthResponse>
}

class LoginUseCaseImpl @Inject constructor(
    private val repository: AuthRepository
): LoginUseCase {
    override suspend fun invoke (email: String, password: String): Result<AuthResponse> =
        repository.login(email, password)
}