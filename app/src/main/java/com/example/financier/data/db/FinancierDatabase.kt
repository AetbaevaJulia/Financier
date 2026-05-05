package com.example.financier.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.financier.data.model.OperationEntity

@Database(
    entities = [
        OperationEntity::class
    ],
    version = 1
)
abstract class FinancierDatabase: RoomDatabase() {
    abstract val operationsDAO: OperationsDAO
}