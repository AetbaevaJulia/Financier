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
//        .baseUrl("http://192.168.137.1:8000/api/")
        .baseUrl("http://10.21.90.107:8000/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create()
}