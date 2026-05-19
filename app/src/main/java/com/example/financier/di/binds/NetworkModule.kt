package com.example.financier.di.binds

import com.example.financier.data.NetworkService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
object NetworkModule {

//    @Provides
//    @Singleton
//    fun provideOkHttpClient(
//
//    ): OkHttpClient {
//        return OkHttpClient.Builder()
//            .connectTimeout(60, TimeUnit.SECONDS)      // Время на установку соединения
//            .readTimeout(120, TimeUnit.SECONDS)        // Время на чтение данных
//            .writeTimeout(120, TimeUnit.SECONDS)       // Время на запись (важно для загрузки файлов)
//            .callTimeout(180, TimeUnit.SECONDS)        // Общий таймаут на весь запрос
//            .retryOnConnectionFailure(true)            // Повторять при ошибках соединения
//            .build()
//    }

    @Provides
    fun provideNetworkService(
//        okHttpClient: OkHttpClient
    ): NetworkService = Retrofit.Builder()
        .baseUrl("http://192.168.43.18:8000/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create()
}