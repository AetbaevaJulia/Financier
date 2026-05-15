package com.example.financier.data.repositories

import com.example.financier.data.NetworkService
import com.example.financier.data.model.FeedbackRequest
import com.example.financier.data.model.OperationResponse
import com.example.financier.data.model.StatementResponse
import retrofit2.Response
import java.util.UUID
import javax.inject.Inject

interface OperationsRepository {
    suspend fun getOperations(statementId: String, token: String): List<OperationResponse>?
    suspend fun updateTransactionFeedback(operationId: String, feedback: FeedbackRequest, token: String): OperationResponse?
}

class OperationsRepositoryImpl @Inject constructor(
    private val service: NetworkService
): OperationsRepository {
    override suspend fun getOperations(statementId: String, token: String): List<OperationResponse>? {
        return try {
            val response: Response<List<OperationResponse>> = service.getOperations(statementId, token)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun updateTransactionFeedback(
        operationId: String,
        feedback: FeedbackRequest,
        token: String
    ): OperationResponse? {
        return try {
            val response: Response<OperationResponse> = service.updateTransactionFeedback(operationId, feedback, token)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}