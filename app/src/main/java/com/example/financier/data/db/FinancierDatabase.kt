package com.example.financier.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.financier.data.mappers.Converters
import com.example.financier.data.model.AnomalyEntity
import com.example.financier.data.model.OperationEntity
import com.example.financier.data.model.RecommendationEntity
import com.example.financier.data.model.RecurringPaymentEntity
import com.example.financier.data.model.ReportEntity
import com.example.financier.data.model.StatementEntity
import com.example.financier.data.model.TopMerchantEntity

@Database(
    entities = [
        OperationEntity::class,
        StatementEntity::class,
        ReportEntity::class,
        RecurringPaymentEntity::class,
        RecommendationEntity::class,
        AnomalyEntity::class,
        TopMerchantEntity::class
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class FinancierDatabase: RoomDatabase() {
    abstract val operationsDAO: OperationsDAO
    abstract val statementsDAO: StatementsDAO
    abstract val reportsDAO: ReportsDAO
}