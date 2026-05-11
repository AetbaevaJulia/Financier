package com.example.financier.data.repositories

import com.example.financier.data.db.OperationsDAO
import com.example.financier.data.model.OperationEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface OperationsRepository {
    fun getOperations(): Flow<List<OperationEntity>>
    fun getOperationsFromTo(startDateTime: Long, endDateTime: Long): Flow<List<OperationEntity>>
    fun getOperationsByCategory(category: String): Flow<List<OperationEntity>>
    suspend fun createOperation(operationEntity: OperationEntity): Long
    suspend fun deleteOperation(operationEntity: OperationEntity): Int
}

class OperationsRepositoryImpl @Inject constructor(
    private val dao: OperationsDAO
): OperationsRepository {
    override fun getOperations(): Flow<List<OperationEntity>> =
        dao.getOperations()

    override fun getOperationsFromTo(
        startDateTime: Long,
        endDateTime: Long
    ): Flow<List<OperationEntity>> =
        dao.getOperationsFromTo(startDateTime, endDateTime)

    override fun getOperationsByCategory(category: String): Flow<List<OperationEntity>> =
        dao.getOperationsByCategory(category)

    override suspend fun createOperation(operationEntity: OperationEntity): Long =
        dao.upsertOperation(operationEntity)

    override suspend fun deleteOperation(operationEntity: OperationEntity): Int =
        dao.deleteOperation(operationEntity)

}