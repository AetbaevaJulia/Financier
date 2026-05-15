package com.example.financier.domain.statementUseCases

import com.example.financier.data.model.StatementResponse
import com.example.financier.data.repositories.StatementRepository
import javax.inject.Inject

interface GetStatementUseCase {
    suspend operator fun invoke(statementId: String, token: String): StatementResponse?
}

class GetStatementUseCaseImpl @Inject constructor(
    private val repository: StatementRepository
): GetStatementUseCase {
    override suspend fun invoke(statementId: String, token: String): StatementResponse? =
        repository.getStatement(statementId, token)
}