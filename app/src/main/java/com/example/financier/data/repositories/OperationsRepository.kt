package com.example.financier.data.repositories

import com.example.financier.data.NetworkService
import com.example.financier.data.model.OperationResponse
import java.util.UUID
import javax.inject.Inject

interface OperationsRepository {
    suspend fun getOperations(statementId: UUID): List<OperationResponse>?
}

class OperationsRepositoryImpl @Inject constructor(
    private val service: NetworkService
): OperationsRepository {
    override suspend fun getOperations(statementId: UUID): List<OperationResponse>? {
        val response = service.getOperations(statementId, "")
        return if (response.isSuccessful)
            return response.body()
        else null
    }
}