package com.example.financier.data.model

data class Report(
    val id: String,
    val statementId: String,
    val totalIncome: Double,
    val totalExpense: Double,
    val netBalance: Double,
    val expenseByCategory: Map<String, Double>,
    val topMerchants: List<TopMerchant>,
    val recurringPayments: List<RecurringPayment>,
    val anomalies: List<Anomaly>,
    val recommendations: List<Recommendation>,
    val generatedAt: String
)

data class RecurringPayment(
    val merchant: String,
    val category: String,
    val averageAmount: Double,
    val count: Int,
    val period: String,
    val totalAmount: Double
)

data class Anomaly(
    val transactionId: String,
    val merchant: String,
    val category: String,
    val amount: Double,
    val reason: String
)

data class Recommendation(
    val title: String,
    val description: String,
    val potentialSaving: Double? = null
)