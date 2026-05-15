package com.example.financier.data.mappers

import com.example.financier.data.model.Anomaly
import com.example.financier.data.model.AnomalyEntity
import com.example.financier.data.model.FullReport
import com.example.financier.data.model.Recommendation
import com.example.financier.data.model.RecommendationEntity
import com.example.financier.data.model.RecurringPayment
import com.example.financier.data.model.RecurringPaymentEntity
import com.example.financier.data.model.Report
import com.example.financier.data.model.ReportEntity
import com.example.financier.data.model.TopMerchant
import com.example.financier.data.model.TopMerchantEntity

fun Report.toEntity(): ReportEntity = ReportEntity(
    reportId = reportId,
    statementId = statementId,
    totalIncome = totalIncome,
    totalExpense = totalExpense,
    netBalance = netBalance,
    expenseByCategory = expenseByCategory,
    generatedAt = generatedAt
)

fun RecurringPayment.toEntity(reportId: String): RecurringPaymentEntity = RecurringPaymentEntity(
    reportId = reportId,
    merchant = merchant,
    category = category,
    averageAmount = averageAmount,
    count = count,
    period = period,
    totalAmount = totalAmount
)

fun Anomaly.toEntity(reportId: String): AnomalyEntity = AnomalyEntity(
    reportId = reportId,
    transactionId = transactionId,
    merchant = merchant,
    category = category,
    amount = amount,
    reason = reason
)

fun Recommendation.toEntity(reportId: String): RecommendationEntity = RecommendationEntity(
    reportId = reportId,
    title = title,
    description = description,
    potentialSaving = potentialSaving
)

fun TopMerchant.toEntity(reportId: String): TopMerchantEntity = TopMerchantEntity(
    reportId = reportId,
    merchant = merchant,
    amount = amount
)

fun FullReport.toResponse(): Report = Report(
    reportId = report.reportId,
    statementId = report.statementId,
    totalIncome = report.totalIncome,
    totalExpense = report.totalExpense,
    netBalance = report.netBalance,
    expenseByCategory = report.expenseByCategory,
    topMerchants = topMerchants.map { it.toResponse() },
    recurringPayments = recurringPayments.map { it.toResponse() },
    anomalies = anomalies.map { it.toResponse() },
    recommendations = recommendations.map { it.toResponse() },
    generatedAt = report.generatedAt
)

fun TopMerchantEntity.toResponse(): TopMerchant = TopMerchant(
    merchant = merchant,
    amount = amount
)

fun RecurringPaymentEntity.toResponse(): RecurringPayment = RecurringPayment(
    merchant = merchant,
    category = category,
    averageAmount = averageAmount,
    count = count,
    period = period,
    totalAmount = totalAmount
)

fun AnomalyEntity.toResponse(): Anomaly = Anomaly(
    transactionId = transactionId,
    merchant = merchant,
    category = category,
    amount = amount,
    reason = reason
)

fun RecommendationEntity.toResponse(): Recommendation = Recommendation(
    title = title,
    description = description,
    potentialSaving = potentialSaving
)