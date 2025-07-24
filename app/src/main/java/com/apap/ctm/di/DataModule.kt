package com.apap.ctm.di

import android.content.Context
import com.apap.ctm.data.db.MonitorDatabase
import com.apap.ctm.data.db.MonitorLogDao
import com.apap.ctm.data.db.MonitorLogEntryDao
import com.apap.ctm.data.db.MonitorRootDao
import com.apap.ctm.data.db.MonitorStatusDao
import com.apap.ctm.data.mapper.MonitorLogEntryMapper
import com.apap.ctm.data.mapper.MonitorLogMapper
import com.apap.ctm.data.mapper.MonitorRootMapper
import com.apap.ctm.data.mapper.MonitorServiceMapper
import com.apap.ctm.data.mapper.MonitorStatusMapper
import com.apap.ctm.data.network.CallTaskController
import com.apap.ctm.data.repository.MonitorLogRepository
import com.apap.ctm.data.repository.MonitorLogRepositoryImpl
import com.apap.ctm.data.repository.MonitorRootRepository
import com.apap.ctm.data.repository.MonitorRootRepositoryImpl
import com.apap.ctm.data.repository.MonitorStatusRepository
import com.apap.ctm.data.repository.MonitorStatusRepositoryImpl
import com.apap.ctm.domain.usecase.GetNameFromContacts
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    companion object {
        @Provides
        @Singleton
        fun provideDatabase(@ApplicationContext context: Context): MonitorDatabase {
            return MonitorDatabase.getInstance(context)
        }

        @Provides
        fun provideLogDao(database: MonitorDatabase): MonitorLogDao = database.logDao()

        @Provides
        fun provideEntriesDao(database: MonitorDatabase): MonitorLogEntryDao = database.entriesDao()

        @Provides
        fun provideRootDao(database: MonitorDatabase): MonitorRootDao = database.rootDao()

        @Provides
        fun provideStatusDao(database: MonitorDatabase): MonitorStatusDao = database.statusDao()

        @Provides
        @Singleton
        fun provideMonitorLogMapper(entryMapper: MonitorLogEntryMapper): MonitorLogMapper = MonitorLogMapper(entryMapper)

        @Provides
        @Singleton
        fun provideMonitorLogEntryMapper(): MonitorLogEntryMapper = MonitorLogEntryMapper()

        @Provides
        @Singleton
        fun provideMonitorRootMapper(
            serviceMapper: MonitorServiceMapper
        ): MonitorRootMapper = MonitorRootMapper(serviceMapper)

        @Provides
        @Singleton
        fun provideMonitorServiceMapper(): MonitorServiceMapper = MonitorServiceMapper()

        @Provides
        @Singleton
        fun provideMonitorStatusMapper(): MonitorStatusMapper = MonitorStatusMapper()

        @Provides
        @Singleton
        fun provideCallTaskController(
            logRepository: MonitorLogRepository,
            rootRepository: MonitorRootRepository,
            statusRepository: MonitorStatusRepository,
            getNameFromContacts: GetNameFromContacts
        ): CallTaskController = CallTaskController(
            logRepository,
            rootRepository,
            statusRepository,
            getNameFromContacts
        )
    }

    @Binds
    abstract fun bindLogRepository(impl: MonitorLogRepositoryImpl): MonitorLogRepository

    @Binds
    abstract fun bindRootRepository(impl: MonitorRootRepositoryImpl): MonitorRootRepository

    @Binds
    abstract fun bindStatusRepository(impl: MonitorStatusRepositoryImpl): MonitorStatusRepository
}