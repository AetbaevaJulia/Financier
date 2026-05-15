package com.example.financier.domain.statementUseCases

import com.example.financier.data.mappers.toEntity
import com.example.financier.data.model.StatementResponse
import com.example.financier.data.repositories.StatementDatabaseRepository
import com.example.financier.data.repositories.StatementRepository
import com.example.financier.enums.StatementStatus
import javax.inject.Inject

interface GetStatementUseCase {
    suspend operator fun invoke(statementId: String, token: String): StatementResponse?
}

class GetStatementUseCaseImpl @Inject constructor(
    private val repository: StatementRepository,
    private val databaseRepository: StatementDatabaseRepository
): GetStatementUseCase {
    override suspend fun invoke(statementId: String, token: String): StatementResponse? {

        val statement = repository.getStatement(statementId, token)

        if (statement != null && statement.status == "report_ready") {
            val localStatement = statement.toEntity()
            databaseRepository.createStatement(localStatement)
        }

        return statement
    }
}