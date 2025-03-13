package com.reyaz.search.data.di

import com.reyaz.search.data.local.RecipeDao
import com.reyaz.search.data.remote.SearchApiService
import com.reyaz.search.data.repository.SearchRepoImpl
import com.reyaz.search.domain.repository.SearchRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

const val BASE_URL = "https://www.themealdb.com/"

@InstallIn(SingletonComponent::class)
@Module
object SearchDataModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://www.themealdb.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideSearchApiService(retrofit: Retrofit): SearchApiService {
        return retrofit.create(SearchApiService::class.java)
    }

    @Provides
    fun provideSearchRepo(
        searchApiService: SearchApiService,
        recipeDao: RecipeDao
    ): SearchRepository {
        return SearchRepoImpl(
            searchApiService = searchApiService, recipeDao = recipeDao
        )
    }
}