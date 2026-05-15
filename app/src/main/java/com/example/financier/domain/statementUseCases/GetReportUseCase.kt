package com.example.financier.domain.statementUseCases

import com.example.financier.data.model.Report
import com.example.financier.data.repositories.StatementRepository
import javax.inject.Inject

interface GetReportUseCase{
    suspend operator fun invoke(statementId: String, token: String): Report?
}

class GetReportUseCaseImpl @Inject constructor(
    private val repository: StatementRepository
) : GetReportUseCase {
    override suspend operator fun invoke(statementId: String, token: String): Report? {
        return repository.getReport(statementId, token)
    }
}