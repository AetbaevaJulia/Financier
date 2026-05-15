package com.example.financier.di.binds

import com.example.financier.data.repositories.StatementRepository
import com.example.financier.data.repositories.StatementRepositoryImpl
import com.example.financier.domain.statementUseCases.GetAllStatementsUseCase
import com.example.financier.domain.statementUseCases.GetAllStatementsUseCaseImpl
import com.example.financier.domain.statementUseCases.GetLatestStatementUseCase
import com.example.financier.domain.statementUseCases.GetLatestStatementUseCaseImpl
import com.example.financier.domain.statementUseCases.GetReportUseCase
import com.example.financier.domain.statementUseCases.GetReportUseCaseImpl
import com.example.financier.domain.statementUseCases.GetStatementUseCase
import com.example.financier.domain.statementUseCases.GetStatementUseCaseImpl
import com.example.financier.domain.statementUseCases.UploadStatementUseCase
import com.example.financier.domain.statementUseCases.UploadStatementUseCaseImpl
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

    @Binds
    @Singleton
    fun bindGetAllStatementsUseCase(impl: GetAllStatementsUseCaseImpl): GetAllStatementsUseCase

    @Binds
    @Singleton
    fun bindGetStatementUseCase(impl: GetStatementUseCaseImpl): GetStatementUseCase

    @Binds
    @Singleton
    fun bindUploadStatementUseCase(impl: UploadStatementUseCaseImpl): UploadStatementUseCase
}