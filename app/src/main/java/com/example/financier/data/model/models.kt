//
//
//@Serializable
//data class RecurringPayment(
//    val merchant: String,
//    val category: String,
//    val averageAmount: Double,
//    val count: Int,
//    val period: String,
//    val totalAmount: Double
//)
//
//@Serializable
//data class Anomaly(
//    val transactionId: UUID,
//    val merchant: String? = null,
//    val category: String,
//    val amount: Double,
//    val reason: String
//)
//
//@Serializable
//data class Recommendation(
//    val title: String,
//    val description: String,
//    val potentialSaving: Double? = null
//)
//
//@Serializable
//data class AnalyticsReport(
//    val id: UUID,
//    val statementId: UUID,
//    val totalIncome: Double,
//    val totalExpense: Double,
//    val netBalance: Double,
//    val expenseByCategory: Map<String, Double>,
//    val topMerchants: List<Map<String, Any>>,
//    val recurringPayments: List<RecurringPayment>,
//    val anomalies: List<Anomaly>,
//    val recommendations: List<Recommendation>,
//    val generatedAt: LocalDateTime
//)
//
//@Serializable
//data class FeedbackRequest(
//    val merchant: String? = null,
//    val category: String? = null,
//    val subcategory: String? = null,
//    val operationType: OperationType? = null
//)
//
//@Serializable
//data class TransactionClassification(
//    val merchant: String,
//    val category: String,
//    val subcategory: String? = null,
//    val confidence: Double
//)
//
//@Serializable
//data class TransactionMapping(
//    val normalizedDescription: String,
//    val merchant: String,
//    val category: String,
//    val subcategory: String? = null,
//    val operationType: OperationType,
//    val confidence: Double,
//    val source: ClassificationSource,
//    val usageCount: Int = 1
//)
