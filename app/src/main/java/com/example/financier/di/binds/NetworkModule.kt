package com.example.financier.di.binds

import com.example.financier.data.NetworkService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

@Module
object NetworkModule {

    @Provides
    fun provideNetworkService(): NetworkService = Retrofit.Builder()
        .baseUrl("http://195.133.53.179:1337")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create()
}