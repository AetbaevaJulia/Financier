package com.example.financier.domain.operationUseCases

import com.example.financier.data.model.OperationEntity
import com.example.financier.data.repositories.OperationsDatabaseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GetOperationsBySubcategoryUseCase {
    operator fun invoke(category: String, startDateTime: Long, endDateTime: Long): Flow<List<OperationEntity>>
}

class GetOperationsBySubcategoryUseCaseImpl @Inject constructor(
    private val repository: OperationsDatabaseRepository
): GetOperationsBySubcategoryUseCase {
    override fun invoke(category: String, startDateTime: Long, endDateTime: Long) =
        repository.getOperationsByCategory(category, startDateTime, endDateTime)
}