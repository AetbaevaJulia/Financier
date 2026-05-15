package com.example.financier.domain.statementUseCases

import android.content.SharedPreferences
import com.example.financier.data.model.StatementResponse
import com.example.financier.data.repositories.StatementRepository
import java.util.UUID
import javax.inject.Inject

interface GetLatestStatementUseCase {
    suspend operator fun invoke(token: String): StatementResponse?
}

class GetLatestStatementUseCaseImpl @Inject constructor(
    private val repository: StatementRepository,
    private val sharedPreferences: SharedPreferences
) : GetLatestStatementUseCase {

    private val testStatementId = "1cb67309-f573-4461-8627-f2262e06e4ef"

    override suspend fun invoke(token: String): StatementResponse? {
        // Для теста, это фейковый StatementResponse

        val userIdString = sharedPreferences.getString("user_id", null)
        val userId = if (!userIdString.isNullOrEmpty()) {
            UUID.fromString(userIdString)
        } else {
            UUID.randomUUID() // fallback
        }

        return StatementResponse(
            id = java.util.UUID.fromString(testStatementId),
            userId = userId,
            filename = "test_statement_recommendations.pdf",
            bank = "sber",
            status = com.example.financier.enums.StatementStatus.REPORT_READY,
            createdAt = java.time.LocalDateTime.now().minusDays(5).toString(),
            updatedAt = java.time.LocalDateTime.now().toString()
        )
    }
}