package com.example.financier.domain.reportUseCases

import com.example.financier.data.mappers.toEntity
import com.example.financier.data.model.AnalyticsRequest
import com.example.financier.data.model.Report
import com.example.financier.data.repositories.ReportDatabaseRepository
import com.example.financier.data.repositories.StatementRepository
import javax.inject.Inject

interface GenerateReportUseCase{
    suspend operator fun invoke(statementId: String, request: AnalyticsRequest?, token: String): Report?
}

class GenerateReportUseCaseImpl @Inject constructor(
    private val repository: StatementRepository,
    private val databaseRepository: ReportDatabaseRepository
) : GenerateReportUseCase {
    override suspend operator fun invoke(statementId: String, request: AnalyticsRequest?, token: String): Report? {

        val report = repository.generateReport(statementId, request, token)

        if (report != null) {
            val localReport = report.toEntity()
            databaseRepository.createReport(localReport)

            report.recurringPayments.forEach {
                databaseRepository.createRecurringPayment(it.toEntity(report.reportId))
            }

            report.anomalies.forEach {
                databaseRepository.createAnomaly(it.toEntity(report.reportId))
            }

            report.recommendations.forEach {
                databaseRepository.createRecommendation(it.toEntity(report.reportId))
            }

            report.topMerchants.forEach {
                databaseRepository.createTopMerchant(it.toEntity(report.reportId))
            }
        }

        return report
    }
}