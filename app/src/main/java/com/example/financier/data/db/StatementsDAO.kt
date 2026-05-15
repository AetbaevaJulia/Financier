package com.example.financier.data.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.financier.data.model.StatementEntity

@Dao
interface StatementsDAO {

    @Upsert
    suspend fun upsertStatement(statementEntity: StatementEntity): Long

    @Query("SELECT * FROM ${StatementEntity.TABLE} " +
            "ORDER BY statementId DESC " +
            "LIMIT 1")
    suspend fun getLastStatement(): StatementEntity?
}