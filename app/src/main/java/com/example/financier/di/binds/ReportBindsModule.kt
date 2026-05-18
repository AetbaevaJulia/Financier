package com.example.financier.di.binds

import com.example.financier.data.db.FinancierDatabase
import com.example.financier.data.db.ReportsDAO
import com.example.financier.data.repositories.ReportDatabaseRepository
import com.example.financier.data.repositories.ReportDatabaseRepositoryImpl
import com.example.financier.domain.reportUseCases.GenerateReportUseCase
import com.example.financier.domain.reportUseCases.GenerateReportUseCaseImpl
import com.example.financier.domain.reportUseCases.GetLastReportUseCase
import com.example.financier.domain.reportUseCases.GetLastReportUseCaseImpl
import com.example.financier.domain.reportUseCases.GetReportUseCase
import com.example.financier.domain.reportUseCases.GetReportUseCaseImpl
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
    fun bindGenerateReportUseCase(impl: GenerateReportUseCaseImpl): GenerateReportUseCase

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

