package com.reyaz.searchrecipeapp.di

import android.content.Context
import com.reyaz.search.navigation.SearchFeatureApi
import com.reyaz.searchrecipeapp.local.AppDatabase
import com.reyaz.searchrecipeapp.navigation.NavigationSubGraphs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import gaur.himanshu.media_player.navigation.MediaPlayerFeatureAPi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideNavigationSubGraph(searchFeatureApi: SearchFeatureApi, mediaPlayerFeatureAPi: MediaPlayerFeatureAPi): NavigationSubGraphs {
        return NavigationSubGraphs(searchFeatureApi, mediaPlayerFeatureApi = mediaPlayerFeatureAPi)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context) = AppDatabase.getInstance(context)

    @Provides
    fun provideRecipeDao(appDatabase: AppDatabase) = appDatabase.getRecipeDao()

}