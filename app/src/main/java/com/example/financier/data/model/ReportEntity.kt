package com.example.financier.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import androidx.room.TypeConverters
import com.example.financier.data.mappers.Converters

@Entity(
    tableName = ReportEntity.TABLE
)
data class ReportEntity(
    @PrimaryKey(autoGenerate = true)
    val Id: Int = 0,
    val reportId: String = "",
    val statementId: String,
    val totalIncome: Double,
    val totalExpense: Double,
    val netBalance: Double,
    val expenseByCategory: Map<String, Double>,
    val generatedAt: String
) {
    companion object {
        const val TABLE = "reports"
    }
}

data class FullReport(
    @Embedded val report: ReportEntity,
    @Relation(
        parentColumn = "reportId",
        entityColumn = "reportId"
    )
    val recurringPayments: List<RecurringPaymentEntity>,
    @Relation(
        parentColumn = "reportId",
        entityColumn = "reportId"
    )
    val anomalies: List<AnomalyEntity>,
    @Relation(
        parentColumn = "reportId",
        entityColumn = "reportId"
    )
    val recommendations: List<RecommendationEntity>,
    @Relation(
        parentColumn = "reportId",
        entityColumn = "reportId"
    )
    val topMerchants: List<TopMerchantEntity>,
)

@Entity(
    tableName = RecurringPaymentEntity.TABLE
)
data class RecurringPaymentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val reportId: String,
    val merchant: String,
    val category: String,
    val averageAmount: Double,
    val count: Int,
    val period: String,
    val totalAmount: Double
) {
    companion object {
        const val TABLE = "recurring_payments"
    }
}

@Entity(
    tableName = AnomalyEntity.TABLE
)
data class AnomalyEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val reportId: String,
    val transactionId: String,
    val merchant: String,
    val category: String,
    val amount: Double,
    val reason: String
) {
    companion object {
        const val TABLE = "anomalies"
    }
}

@Entity(
    tableName = RecommendationEntity.TABLE
)
data class RecommendationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val reportId: String,
    val title: String,
    val description: String,
    val potentialSaving: Double? = null
) {
    companion object {
        const val TABLE = "recommendations"
    }
}

@Entity(
    tableName = TopMerchantEntity.TABLE
)
data class TopMerchantEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val reportId: String,
    val merchant: String,
    val amount: Double
) {
    companion object {
        const val TABLE = "top_merchants"
    }
}