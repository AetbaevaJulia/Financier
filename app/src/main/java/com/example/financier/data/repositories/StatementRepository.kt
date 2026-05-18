package com.example.financier.data.repositories

import com.example.financier.data.NetworkService
import com.example.financier.data.model.AnalyticsRequest
import com.example.financier.data.model.OperationResponse
import com.example.financier.data.model.Report
import com.example.financier.data.model.StatementResponse
import com.example.financier.data.model.UploadStatementResponse
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject

interface StatementRepository {
    suspend fun getStatement(statementId: String, token: String): StatementResponse?
    suspend fun getAllStatements(token: String): List<StatementResponse>?
    suspend fun getReport(statementId: String, token: String): Report?
    suspend fun generateReport(statementId: String, request: AnalyticsRequest?, token: String): Report?
    suspend fun uploadStatement(file: MultipartBody.Part, token: String): List<OperationResponse>?
}

class StatementRepositoryImpl @Inject constructor(
    private val service: NetworkService
) : StatementRepository {
    override suspend fun getStatement(
        statementId: String,
        token: String
    ): StatementResponse? {
        return try {
            val response: Response<StatementResponse> = service.getStatementStatus(statementId, token)
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

    override suspend fun getAllStatements(
        token: String
    ): List<StatementResponse>? {
        return try {
            val response: Response<List<StatementResponse>> = service.listStatements( token)
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

    override suspend fun generateReport(
        statementId: String,
        request: AnalyticsRequest?,
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

    override suspend fun uploadStatement(
        file: MultipartBody.Part,
        token: String
    ): List<OperationResponse>? {
        return try {
            val response: Response<List<OperationResponse>> = service.uploadStatement(file, token)
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