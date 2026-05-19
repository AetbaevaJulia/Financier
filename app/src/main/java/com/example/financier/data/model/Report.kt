package com.example.financier.data.model

import com.google.gson.annotations.SerializedName

data class Report(
    @SerializedName("id")
    val reportId: String,
    @SerializedName("statement_id")
    val statementId: String,
    @SerializedName("total_income")
    val totalIncome: Double,
    @SerializedName("total_expense")
    val totalExpense: Double,
    @SerializedName("net_balance")
    val netBalance: Double,
    @SerializedName("expense_by_category")
    var expenseByCategory: Map<String, Double>,
    @SerializedName("top_merchants")
    val topMerchants: List<TopMerchant>,
    @SerializedName("recurring_payments")
    val recurringPayments: List<RecurringPayment>,
    @SerializedName("anomalies")
    val anomalies: List<Anomaly>,
    @SerializedName("recommendations")
    val recommendations: List<Recommendation>,
    @SerializedName("generated_at")
    val generatedAt: String
)

data class RecurringPayment(

    @SerializedName("merchant")
    val merchant: String,
    @SerializedName("category")
    val category: String,
    @SerializedName("average_amount")
    val averageAmount: Double,
    @SerializedName("count")
    val count: Int,
    @SerializedName("period")
    val period: String,
    @SerializedName("total_amount")
    val totalAmount: Double
)

data class Anomaly(
    @SerializedName("transaction_id")
    val transactionId: String,
    @SerializedName("merchant")
    val merchant: String,
    @SerializedName("category")
    val category: String,
    @SerializedName("amount")
    val amount: Double,
    @SerializedName("reason")
    val reason: String
)

data class Recommendation(
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("potential_saving")
    val potentialSaving: Double? = null
)