package com.example.financier.di.binds

import com.example.financier.data.repositories.StatementRepository
import com.example.financier.data.repositories.StatementRepositoryImpl
import com.example.financier.domain.reportUseCase.GetLatestStatementUseCase
import com.example.financier.domain.reportUseCase.GetLatestStatementUseCaseImpl
import com.example.financier.domain.reportUseCase.GetReportUseCase
import com.example.financier.domain.reportUseCase.GetReportUseCaseImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface StatementBindsModule {
    @Binds
    @Singleton
    fun bindStatementRepository(impl: StatementRepositoryImpl): StatementRepository

    @Binds
    @Singleton
    fun bindGetReportUseCase(impl: GetReportUseCaseImpl): GetReportUseCase

    @Binds
    @Singleton
    fun bindGetLatestStatementUseCase(impl: GetLatestStatementUseCaseImpl): GetLatestStatementUseCase

}