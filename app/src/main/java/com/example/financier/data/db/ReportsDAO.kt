package com.example.financier.data.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.financier.data.model.AnomalyEntity
import com.example.financier.data.model.FullReport
import com.example.financier.data.model.RecommendationEntity
import com.example.financier.data.model.RecurringPaymentEntity
import com.example.financier.data.model.ReportEntity
import com.example.financier.data.model.TopMerchantEntity

@Dao
interface ReportsDAO {

    @Upsert
    suspend fun upsertReport(reportEntity: ReportEntity): Long

    @Upsert
    suspend fun upsertRecurringPayment(recurringPaymentEntity: RecurringPaymentEntity): Long
    @Upsert
    suspend fun upsertRecommendation(recommendationEntity: RecommendationEntity): Long
    @Upsert
    suspend fun upsertAnomaly(anomalyEntity: AnomalyEntity): Long
    @Upsert
    suspend fun upsertTopMerchant(topMerchantEntity: TopMerchantEntity): Long

    @Transaction
    @Query("SELECT * FROM ${ReportEntity.TABLE} " +
            "ORDER BY Id " +
            "LIMIT 1")
    suspend fun getLastReport(): FullReport?
}