package com.example.financier.domain.operationUseCases

import com.example.financier.data.mappers.toEntity
import com.example.financier.data.model.OperationEntity
import com.example.financier.data.model.OperationResponse
import com.example.financier.data.repositories.OperationsDatabaseRepository
import com.example.financier.data.repositories.OperationsRepository
import java.sql.Date
import java.sql.Time
import javax.inject.Inject

interface GetStatementOperationsUseCase {
    suspend operator fun invoke(statementId: String, token: String): List<OperationResponse>?
}

class GetStatementOperationsUseCaseImpl @Inject constructor(
    private val repository: OperationsRepository,
    private val databaseRepository: OperationsDatabaseRepository
): GetStatementOperationsUseCase {
    override suspend fun invoke(statementId: String, token: String): List<OperationResponse>? {

        val operations = repository.getOperations(statementId, token)

        operations?.forEach { remoteOperation ->
            val localOperation = remoteOperation.toEntity()

            databaseRepository.createOperation(localOperation)
        }

        return operations
    }
}