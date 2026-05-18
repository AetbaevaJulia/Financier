package com.example.financier.domain.reportUseCases

import com.example.financier.data.mappers.toResponse
import com.example.financier.data.model.Report
import com.example.financier.data.repositories.ReportDatabaseRepository
import javax.inject.Inject

interface GetLastReportUseCase{
    suspend operator fun invoke(): Report?
}

class GetLastReportUseCaseImpl @Inject constructor(
    private val databaseRepository: ReportDatabaseRepository
): GetLastReportUseCase {
    override suspend fun invoke(): Report? {
        val report = databaseRepository.getLastReport()

        return report?.toResponse()
    }
}