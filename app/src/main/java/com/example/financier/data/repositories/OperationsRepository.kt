package com.example.financier.data.repositories

import com.example.financier.data.NetworkService
import com.example.financier.data.model.OperationResponse
import java.util.UUID
import javax.inject.Inject

interface OperationsRepository {
    suspend fun getOperations(statement_id: UUID): List<OperationResponse>
}

class OperationsRepositoryImpl @Inject constructor(
    private val service: NetworkService
): OperationsRepository {
    override suspend fun getOperations(statement_id: UUID): List<OperationResponse> {
        val response = service.getTransactions(statement_id, "")
        return if (response.isSuccessful)
            return response.body()
        else null
    }
}