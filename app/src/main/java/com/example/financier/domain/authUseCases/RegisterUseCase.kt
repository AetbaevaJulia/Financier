package com.example.financier.domain.authUseCases

import com.example.financier.data.model.AuthResponse
import com.example.financier.data.model.OperationEntity
import com.example.financier.data.repositories.OperationsRepository
import com.example.financier.data.repository.AuthRepository
import javax.inject.Inject

interface RegisterUseCase {
    suspend operator fun invoke(email: String, password: String): Result<AuthResponse>
}

class RegisterUseCaseImpl @Inject constructor(
    private val repository: AuthRepository
): RegisterUseCase {
    override suspend fun invoke (email: String, password: String): Result<AuthResponse> =
        repository.register(email, password)
}