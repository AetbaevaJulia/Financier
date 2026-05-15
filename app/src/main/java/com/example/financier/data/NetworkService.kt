package com.example.financier.data

import com.example.financier.data.model.AuthResponse
import com.example.financier.data.model.FeedbackRequest
import com.example.financier.data.model.LoginRequest
import com.example.financier.data.model.OperationResponse
import com.example.financier.data.model.RegisterRequest
import com.example.financier.data.model.Report
import com.example.financier.data.model.StatementResponse
import com.example.financier.data.model.UploadStatementResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import java.util.UUID

interface NetworkService {

    @GET("/health")
    suspend fun health(): Map<String, String>

    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<AuthResponse>

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<AuthResponse>

    @Multipart
    @POST("statements")
    suspend fun uploadStatement(
        @Part file: MultipartBody.Part,
        @Header("Authorization") authorization: String
    ): Response<UploadStatementResponse>

    @GET("statements")
    suspend fun listStatements(
        @Header("Authorization") authorization: String
    ): Response<List<StatementResponse>>

    @GET("statements/{statement_id}/status")
    suspend fun getStatementStatus(
        @Path("statement_id") statementId: String,
        @Header("Authorization") authorization: String
    ): Response<StatementResponse>

    @GET("statements/{statement_id}/transactions")
    suspend fun getOperations(
        @Path("statement_id") statementId: String,
        @Header("Authorization") authorization: String
    ): Response<List<OperationResponse>>

    @GET("statements/{statement_id}/report")
    suspend fun getReport(
        @Path("statement_id") statementId: String,
        @Header("Authorization") authorization: String
    ): Response<Report>

    @PATCH("transactions/{transaction_id}/feedback") //Изменение данных о транзакции
    suspend fun updateTransactionFeedback(
        @Path("transaction_id") operationId: String,
        @Body request: FeedbackRequest,
        @Header("Authorization") authorization: String
    ): Response<OperationResponse>
}