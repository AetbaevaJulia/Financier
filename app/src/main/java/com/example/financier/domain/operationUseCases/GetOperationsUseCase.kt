package com.example.financier.domain.operationUseCases

import com.example.financier.data.OperationsRepository
import com.example.financier.data.model.OperationEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GetOperationsUseCase {
    operator fun invoke(): Flow<List<OperationEntity>>
}

class GetOperationsUseCaseImpl @Inject constructor(
    private val repository: OperationsRepository
): GetOperationsUseCase {
    override fun invoke(): Flow<List<OperationEntity>> =
        repository.getOperations()
}