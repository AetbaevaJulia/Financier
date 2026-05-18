package com.example.financier.domain.statementUseCases

import com.example.financier.data.mappers.toResponse
import com.example.financier.data.model.UploadStatementResponse
import com.example.financier.data.repositories.StatementRepository
import javax.inject.Inject

interface GetLastStatementFromBackUseCase {
    suspend operator fun invoke(token: String): UploadStatementResponse?
}

class GetLastStatementFromBackUseCaseImpl @Inject constructor(
    private val repository: StatementRepository
) : GetLastStatementFromBackUseCase {

    override suspend fun invoke(token: String): UploadStatementResponse? {
        val statements = repository.getAllStatements(token)

        return if (statements != null) {
            UploadStatementResponse(statements[0].statementId, "uploading")
        } else null
    }
}