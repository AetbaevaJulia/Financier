package com.example.financier.domain.statementUseCases

import com.example.financier.data.mappers.toEntity
import com.example.financier.data.model.StatementResponse
import com.example.financier.data.repositories.StatementDatabaseRepository
import com.example.financier.data.repositories.StatementRepository
import javax.inject.Inject

interface GetAllStatementsUseCase {
    suspend operator fun invoke(token: String): List<StatementResponse>?
}

class GetAllStatementsUseCaseImpl @Inject constructor(
    private val repository: StatementRepository,
    private val databaseRepository: StatementDatabaseRepository
): GetAllStatementsUseCase {
    override suspend fun invoke(token: String): List<StatementResponse>? {

        val statements = repository.getAllStatements(token)

        statements?.forEach { remoteStatement ->
            val localStatement = remoteStatement.toEntity()

            databaseRepository.createStatement(localStatement)
        }

        return statements
    }
}