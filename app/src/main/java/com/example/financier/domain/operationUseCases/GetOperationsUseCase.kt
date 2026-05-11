package com.example.financier.domain.operationUseCases

import com.example.financier.data.repositories.OperationsDatabaseRepository
import com.example.financier.data.model.OperationEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GetOperationsUseCase {
    operator fun invoke(): Flow<List<OperationEntity>>
}

class GetOperationsUseCaseImpl @Inject constructor(
    private val repository: OperationsDatabaseRepository
): GetOperationsUseCase {
    override fun invoke(): Flow<List<OperationEntity>> =
        repository.getOperations()
}