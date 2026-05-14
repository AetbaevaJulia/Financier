package com.example.financier.data.repositories

import com.example.financier.data.NetworkService
import com.example.financier.data.model.Report
import retrofit2.Response
import javax.inject.Inject

interface StatementRepository {
    suspend fun getReport(statementId: String, token: String): Report?
}

class StatementRepositoryImpl @Inject constructor(
    private val service: NetworkService
) : StatementRepository {

    override suspend fun getReport(
        statementId: String,
        token: String
    ): Report? {
        return try {
            val response: Response<Report> = service.getReport(statementId, token)
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