package com.example.financier.di.binds

import android.app.Application
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppBindsModule {

    companion object {
        @Provides
        fun provideContext(app: Application): Context =
            app.applicationContext

//        @Provides
//        @Singleton
//        fun provideDb(context: Context): NotesDatabase =
//            Room.databaseBuilder(
//                context,
//                NotesDatabase::class.java,
//                "notes.db"
//            ).build()
    }
}