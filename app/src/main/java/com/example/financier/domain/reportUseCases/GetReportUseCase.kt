package com.example.financier.domain.reportUseCases

import android.util.Log
import com.example.financier.data.mappers.toEntity
import com.example.financier.data.model.AnalyticsRequest
import com.example.financier.data.model.Report
import com.example.financier.data.repositories.ReportDatabaseRepository
import com.example.financier.data.repositories.StatementRepository
import javax.inject.Inject

interface GetReportUseCase{
    suspend operator fun invoke(statementId: String, token: String): Report?
}

class GetReportUseCaseImpl @Inject constructor(
    private val repository: StatementRepository,
    private val databaseRepository: ReportDatabaseRepository
) : GetReportUseCase {
    override suspend operator fun invoke(statementId: String, token: String): Report? {

        var report = repository.getReport(statementId, token)

        Log.d("Загруженный репорт", report.toString())
        if (report == null) {
            report = repository.generateReport(statementId, AnalyticsRequest(), token = token)
            Log.d("Загруженный аналитик", report.toString())
        }

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