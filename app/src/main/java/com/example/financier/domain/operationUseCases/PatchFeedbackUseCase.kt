package com.example.financier.domain.operationUseCases

import com.example.financier.data.model.FeedbackRequest
import com.example.financier.data.model.OperationResponse
import com.example.financier.data.repositories.OperationsRepository
import javax.inject.Inject

interface PatchFeedbackUseCase {
    suspend operator fun invoke(operationId: String, feedback: FeedbackRequest, token: String): OperationResponse?
}

class PatchFeedbackUseCaseImpl @Inject constructor(
    private val repository: OperationsRepository
): PatchFeedbackUseCase {
    override suspend fun invoke(operationId: String, feedback: FeedbackRequest, token: String): OperationResponse? =
        repository.updateTransactionFeedback(operationId, feedback, token)
}