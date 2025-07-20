package com.apap.ctm.di

import android.content.Context
import androidx.room.Room
import com.apap.ctm.data.db.MonitorDatabase
import com.apap.ctm.data.db.MonitorLogDao
import com.apap.ctm.data.db.MonitorRootDao
import com.apap.ctm.data.db.MonitorStatusDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MonitorDatabase {
        return Room.databaseBuilder(context, MonitorDatabase::class.java, "monitor.db")
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }

    @Provides
    fun provideLogDao(database: MonitorDatabase): MonitorLogDao = database.logDao()

    @Provides
    fun provideRootDao(database: MonitorDatabase): MonitorRootDao = database.rootDao()

    @Provides
    fun provideStatusDao(database: MonitorDatabase): MonitorStatusDao = database.statusDao()
}