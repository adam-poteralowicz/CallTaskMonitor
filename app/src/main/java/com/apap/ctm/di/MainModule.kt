package com.apap.ctm.di

import android.content.Context
import com.apap.ctm.util.ResourcesProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
class MainModule {

    @Provides
    fun resourcesProvider(@ApplicationContext context: Context) = ResourcesProvider(context)
}