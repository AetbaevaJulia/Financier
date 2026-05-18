package com.example.financier.data.repositories

import com.example.financier.data.db.OperationsDAO
import com.example.financier.data.model.OperationEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface OperationsDatabaseRepository {
    fun getOperations(): Flow<List<OperationEntity>>
    fun getOperationsFromTo(startDateTime: Long, endDateTime: Long): Flow<List<OperationEntity>>
    fun getOperationsByCategory(category: String, startDateTime: Long, endDateTime: Long): Flow<List<OperationEntity>>
    fun getOperationsBySubCategory(subcategory: String, startDateTime: Long, endDateTime: Long): Flow<List<OperationEntity>>
    suspend fun createOperation(operationEntity: OperationEntity): Long
    suspend fun deleteOperation(operationEntity: OperationEntity): Int
}

class OperationsDatabaseRepositoryImpl @Inject constructor(
    private val dao: OperationsDAO
): OperationsDatabaseRepository {
    override fun getOperations(): Flow<List<OperationEntity>> =
        dao.getOperations()

    override fun getOperationsFromTo(
        startDateTime: Long,
        endDateTime: Long
    ): Flow<List<OperationEntity>> =
        dao.getOperationsFromTo(startDateTime, endDateTime)

    override fun getOperationsByCategory(category: String, startDateTime: Long, endDateTime: Long): Flow<List<OperationEntity>> =
        dao.getOperationsByCategoryAndTime(category, startDateTime, endDateTime)

    override fun getOperationsBySubCategory(
        subcategory: String,
        startDateTime: Long,
        endDateTime: Long
    ): Flow<List<OperationEntity>> =
        dao.getOperationsBySubcategoryAndTime(subcategory, startDateTime, endDateTime)

    override suspend fun createOperation(operationEntity: OperationEntity): Long =
        dao.upsertOperation(operationEntity)

    override suspend fun deleteOperation(operationEntity: OperationEntity): Int =
        dao.deleteOperation(operationEntity)

}