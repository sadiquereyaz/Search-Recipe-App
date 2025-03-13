package com.reyaz.search.di

import com.reyaz.search.navigation.SearchFeatureApi
import com.reyaz.search.navigation.SearchFeatureApiImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object UiModule {
    @Provides
    fun provideSearchFeatureApi(): SearchFeatureApi = SearchFeatureApiImpl()

}