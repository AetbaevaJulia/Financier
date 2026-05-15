package com.example.financier.data.repositories

import com.example.financier.data.db.ReportsDAO
import com.example.financier.data.model.AnomalyEntity
import com.example.financier.data.model.FullReport
import com.example.financier.data.model.RecommendationEntity
import com.example.financier.data.model.RecurringPaymentEntity
import com.example.financier.data.model.ReportEntity
import com.example.financier.data.model.TopMerchantEntity
import javax.inject.Inject

interface ReportDatabaseRepository {
    suspend fun createReport(reportEntity: ReportEntity): Long
    suspend fun getLastReport(): FullReport?
    suspend fun createRecommendation(recommendationEntity: RecommendationEntity): Long
    suspend fun createRecurringPayment(recurringPaymentEntity: RecurringPaymentEntity): Long
    suspend fun createAnomaly(anomalyEntity: AnomalyEntity): Long
    suspend fun createTopMerchant(topMerchantEntity: TopMerchantEntity): Long
}

class ReportDatabaseRepositoryImpl @Inject constructor(
    private val dao: ReportsDAO
): ReportDatabaseRepository {
    override suspend fun createReport(reportEntity: ReportEntity): Long =
        dao.upsertReport(reportEntity)

    override suspend fun createRecommendation(recommendationEntity: RecommendationEntity): Long =
        dao.upsertRecommendation(recommendationEntity)
    override suspend fun createRecurringPayment(recurringPaymentEntity: RecurringPaymentEntity): Long =
        dao.upsertRecurringPayment(recurringPaymentEntity)
    override suspend fun createAnomaly(anomalyEntity: AnomalyEntity): Long =
        dao.upsertAnomaly(anomalyEntity)
    override suspend fun createTopMerchant(topMerchantEntity: TopMerchantEntity): Long =
        dao.upsertTopMerchant(topMerchantEntity)


    override suspend fun getLastReport(): FullReport? =
        dao.getLastReport()
}