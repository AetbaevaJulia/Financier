package com.example.financier.domain.operationUseCases

import com.example.financier.data.repositories.OperationsRepository
import com.example.financier.data.model.OperationEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GetOperationsFromToUseCase {
    operator fun invoke(startDateTime: Long, endDateTime: Long): Flow<List<OperationEntity>>
}

class GetOperationsFromToUseCaseImpl @Inject constructor(
    private val repository: OperationsRepository
): GetOperationsFromToUseCase {
    override fun invoke(
        startDateTime: Long,
        endDateTime: Long
    ): Flow<List<OperationEntity>> =
        repository.getOperationsFromTo(startDateTime, endDateTime)
}