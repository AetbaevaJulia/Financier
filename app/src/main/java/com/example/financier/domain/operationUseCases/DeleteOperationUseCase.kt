package com.example.financier.domain.operationUseCases

import com.example.financier.data.repositories.OperationsRepository
import com.example.financier.data.model.OperationEntity
import javax.inject.Inject

interface DeleteOperationUseCase {
    suspend operator fun invoke(operationEntity: OperationEntity): Int
}

class DeleteOperationUseCaseImpl @Inject constructor(
    private val repository: OperationsRepository
): DeleteOperationUseCase {
    override suspend fun invoke(operationEntity: OperationEntity): Int =
        repository.deleteOperation(operationEntity)
}