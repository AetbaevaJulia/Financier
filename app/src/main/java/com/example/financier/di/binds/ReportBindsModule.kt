package com.example.financier.di.binds

import com.example.financier.data.db.FinancierDatabase
import com.example.financier.data.db.ReportsDAO
import com.example.financier.data.repositories.ReportDatabaseRepository
import com.example.financier.data.repositories.ReportDatabaseRepositoryImpl
import com.example.financier.domain.statementUseCases.GetLastReportUseCase
import com.example.financier.domain.statementUseCases.GetLastReportUseCaseImpl
import com.example.financier.domain.statementUseCases.GetReportUseCase
import com.example.financier.domain.statementUseCases.GetReportUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
interface ReportBindsModule {

    @Binds
    @Singleton
    fun bindReportDatabaseRepository(impl: ReportDatabaseRepositoryImpl): ReportDatabaseRepository

    @Binds
    @Singleton
    fun bindGetReportUseCase(impl: GetReportUseCaseImpl): GetReportUseCase

    @Binds
    @Singleton
    fun bindGetLastReportUseCase(impl: GetLastReportUseCaseImpl): GetLastReportUseCase

    companion object {

        @Provides
        @Singleton
        fun provideReportsDAO(
            db: FinancierDatabase
        ): ReportsDAO =
            db.reportsDAO
    }
}

