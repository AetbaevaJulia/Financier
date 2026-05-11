package com.example.financier.domain.operationUseCases

import com.example.financier.data.repositories.OperationsDatabaseRepository
import com.example.financier.data.model.OperationEntity
import javax.inject.Inject

interface CreateOperationUseCase {
    suspend operator fun invoke(operationEntity: OperationEntity): Long
}

class CreateOperationUseCaseImpl @Inject constructor(
    private val repository: OperationsDatabaseRepository
): CreateOperationUseCase {
    override suspend fun invoke(operationEntity: OperationEntity): Long =
        repository.createOperation(operationEntity)
}