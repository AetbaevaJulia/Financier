package com.example.financier.domain.operationUseCases

import com.example.financier.data.OperationsRepository
import com.example.financier.data.model.OperationEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GetOperationsByCategoryUseCase {
    operator fun invoke(category: String): Flow<List<OperationEntity>>
}

class GetOperationsByCategoryUseCaseImpl @Inject constructor(
    private val repository: OperationsRepository
): GetOperationsByCategoryUseCase {
    override fun invoke(category: String) =
        repository.getOperationsByCategory(category)
}