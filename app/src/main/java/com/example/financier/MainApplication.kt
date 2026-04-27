package com.example.financier

import android.app.Application
import android.content.Context
import com.example.financier.di.AppComponent
import com.example.financier.di.DaggerAppComponent

class MainApplication: Application() {
    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        appComponent = DaggerAppComponent.builder()
            .application(this)
            .build()

        super.onCreate()
    }
}

val Context.appComponent: AppComponent
    get() = when(this) {
        is MainApplication -> this.appComponent
        else -> this.applicationContext.appComponent
    }