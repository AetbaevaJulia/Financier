package com.example.financier.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.financier.data.model.OperationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OperationsDAO {

    @Query("SELECT * FROM ${OperationEntity.TABLE}")
    fun getOperations(): Flow<List<OperationEntity>>

    @Query("SELECT * FROM ${OperationEntity.TABLE} " +
            "WHERE category = :category")
    fun getOperationsByCategory(category: String): Flow<List<OperationEntity>>

    @Query("SELECT * FROM ${OperationEntity.TABLE} " +
            "WHERE operationDate BETWEEN :startDateTime AND :endDateTime")
    fun getOperationsFromTo(startDateTime: Long, endDateTime: Long): Flow<List<OperationEntity>>

    @Upsert
    suspend fun upsertOperation(operationEntity: OperationEntity): Long

    @Delete
    suspend fun deleteOperation(operationEntity: OperationEntity): Int
}