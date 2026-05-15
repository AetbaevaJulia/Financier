package com.example.financier.domain.operationUseCases

import com.example.financier.data.model.OperationResponse
import com.example.financier.data.repositories.OperationsRepository
import javax.inject.Inject

interface GetStatementOperationsUseCase {
    suspend operator fun invoke(statementId: String, token: String): List<OperationResponse>?
}

class GetStatementOperationsUseCaseImpl @Inject constructor(
    private val repository: OperationsRepository
): GetStatementOperationsUseCase {
    override suspend fun invoke(statementId: String, token: String): List<OperationResponse>? =
        repository.getOperations(statementId, token)
}