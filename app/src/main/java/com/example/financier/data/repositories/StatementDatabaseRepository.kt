package com.example.financier.data.repositories

import com.example.financier.data.db.StatementsDAO
import com.example.financier.data.model.StatementEntity
import javax.inject.Inject

interface StatementDatabaseRepository {
    suspend fun createStatement(statementEntity: StatementEntity): Long
    suspend fun getLastStatement(): StatementEntity?
}

class StatementDatabaseRepositoryImpl @Inject constructor(
    private val dao: StatementsDAO
): StatementDatabaseRepository {
    override suspend fun createStatement(statementEntity: StatementEntity): Long =
        dao.upsertStatement(statementEntity)

    override suspend fun getLastStatement(): StatementEntity? =
        dao.getLastStatement()
}
