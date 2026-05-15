package com.example.financier.di.binds

import com.example.financier.data.repository.AuthRepository
import com.example.financier.data.repository.AuthRepositoryImpl
import com.example.financier.domain.authUseCases.LoginUseCase
import com.example.financier.domain.authUseCases.LoginUseCaseImpl
import com.example.financier.domain.authUseCases.RegisterUseCase
import com.example.financier.domain.authUseCases.RegisterUseCaseImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface AuthBindsModule {

    @Binds
    @Singleton
    fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    fun bindRegisterUseCase(impl: RegisterUseCaseImpl): RegisterUseCase

    @Binds
    @Singleton
    fun bindLoginUseCase(impl: LoginUseCaseImpl): LoginUseCase

}