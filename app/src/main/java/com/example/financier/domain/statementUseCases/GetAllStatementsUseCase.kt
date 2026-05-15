package com.example.financier.domain.statementUseCases

import com.example.financier.data.model.StatementResponse
import com.example.financier.data.repositories.StatementRepository
import javax.inject.Inject

interface GetAllStatementsUseCase {
    suspend operator fun invoke(token: String): List<StatementResponse>?
}

class GetAllStatementsUseCaseImpl @Inject constructor(
    private val repository: StatementRepository
): GetAllStatementsUseCase {
    override suspend fun invoke(token: String): List<StatementResponse>? =
        repository.getAllStatements(token)
}