package com.example.financier.domain.statementUseCases

import com.example.financier.data.mappers.toResponse
import com.example.financier.data.model.StatementResponse
import com.example.financier.data.repositories.StatementDatabaseRepository
import javax.inject.Inject

interface GetLatestStatementUseCase {
    suspend operator fun invoke(): StatementResponse?
}

class GetLatestStatementUseCaseImpl @Inject constructor(
    private val databaseRepository: StatementDatabaseRepository
) : GetLatestStatementUseCase {

    override suspend fun invoke(): StatementResponse? =
        databaseRepository.getLastStatement()?.toResponse()
}