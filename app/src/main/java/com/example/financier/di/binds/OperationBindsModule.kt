package com.example.financier.di.binds

import com.example.financier.data.repositories.OperationsDatabaseRepository
import com.example.financier.data.repositories.OperationsDatabaseRepositoryImpl
import com.example.financier.data.repositories.OperationsRepository
import com.example.financier.data.repositories.OperationsRepositoryImpl
import com.example.financier.domain.operationUseCases.PatchFeedbackUseCase
import com.example.financier.domain.operationUseCases.PatchFeedbackUseCaseImpl
import com.example.financier.domain.operationUseCases.CreateOperationUseCase
import com.example.financier.domain.operationUseCases.CreateOperationUseCaseImpl
import com.example.financier.domain.operationUseCases.DeleteOperationUseCase
import com.example.financier.domain.operationUseCases.DeleteOperationUseCaseImpl
import com.example.financier.domain.operationUseCases.GetOperationsByCategoryUseCase
import com.example.financier.domain.operationUseCases.GetOperationsByCategoryUseCaseImpl
import com.example.financier.domain.operationUseCases.GetOperationsFromToUseCase
import com.example.financier.domain.operationUseCases.GetOperationsFromToUseCaseImpl
import com.example.financier.domain.operationUseCases.GetOperationsUseCase
import com.example.financier.domain.operationUseCases.GetOperationsUseCaseImpl
import com.example.financier.domain.operationUseCases.GetStatementOperationsUseCase
import com.example.financier.domain.operationUseCases.GetStatementOperationsUseCaseImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface OperationBindsModule {

    @Binds
    @Singleton
    fun bindOperationsDatabaseRepository(impl: OperationsDatabaseRepositoryImpl): OperationsDatabaseRepository

    @Binds
    @Singleton
    fun bindOperationsRepository(impl: OperationsRepositoryImpl): OperationsRepository

    @Binds
    @Singleton
    fun bindGetOperationsUseCase(impl: GetOperationsUseCaseImpl): GetOperationsUseCase

    @Binds
    @Singleton
    fun bindGetOperationsByCategoryUseCase(impl: GetOperationsByCategoryUseCaseImpl): GetOperationsByCategoryUseCase

    @Binds
    @Singleton
    fun bindGetOperationsFromToUseCase(impl: GetOperationsFromToUseCaseImpl): GetOperationsFromToUseCase

    @Binds
    @Singleton
    fun bindCreateOperationUseCase(impl: CreateOperationUseCaseImpl): CreateOperationUseCase

    @Binds
    @Singleton
    fun bindDeleteOperationUseCase(impl: DeleteOperationUseCaseImpl): DeleteOperationUseCase

    @Binds
    @Singleton
    fun bindGetStatementOperationsUseCase(impl: GetStatementOperationsUseCaseImpl): GetStatementOperationsUseCase

    @Binds
    @Singleton
    fun bindPatchFeedbackUseCase(impl: PatchFeedbackUseCaseImpl): PatchFeedbackUseCase
}