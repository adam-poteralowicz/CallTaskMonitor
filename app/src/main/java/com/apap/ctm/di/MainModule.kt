package com.apap.ctm.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
object MainModule {

    @Provides
    fun provideIoDispatcher() : CoroutineDispatcher = Dispatchers.IO

    @Provides
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }
}