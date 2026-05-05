package com.example.financier.di.binds

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.financier.data.db.FinancierDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
interface AppBindsModule {

    companion object {
        @Provides
        fun provideContext(app: Application): Context =
            app.applicationContext

        @Provides
        @Singleton
        fun provideDb(context: Context): FinancierDatabase =
            Room.databaseBuilder(
                context,
                FinancierDatabase::class.java,
                "notes.db"
            ).build()
    }
}