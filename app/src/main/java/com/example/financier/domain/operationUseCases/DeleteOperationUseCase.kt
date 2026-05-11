package com.example.financier.domain.operationUseCases

import com.example.financier.data.repositories.OperationsDatabaseRepository
import com.example.financier.data.model.OperationEntity
import javax.inject.Inject

interface DeleteOperationUseCase {
    suspend operator fun invoke(operationEntity: OperationEntity): Int
}

class DeleteOperationUseCaseImpl @Inject constructor(
    private val repository: OperationsDatabaseRepository
): DeleteOperationUseCase {
    override suspend fun invoke(operationEntity: OperationEntity): Int =
        repository.deleteOperation(operationEntity)
}