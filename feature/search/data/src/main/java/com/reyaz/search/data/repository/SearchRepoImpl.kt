package com.reyaz.search.data.repository

import com.reyaz.search.data.local.RecipeDao
import com.reyaz.search.data.mapper.toDomain
import com.reyaz.search.data.remote.SearchApiService
import com.reyaz.search.domain.model.Recipe
import com.reyaz.search.domain.model.RecipeDetail
import com.reyaz.search.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow

class SearchRepoImpl(
    private val searchApiService: SearchApiService,
    private val recipeDao: RecipeDao
) : SearchRepository {
    override suspend fun getRecipes(s: String): Result<List<Recipe>> {
        return try {
            val response = searchApiService.getRecipes(s)
            return if (response.isSuccessful) {
                response.body()?.meals?.let {
                    Result.success(it.toDomain())
                } ?: run { Result.failure(Exception("No recipes found")) }
            } else {
                Result.failure(Exception("Failed to fetch recipes"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getRecipeDetails(id: String): Result<RecipeDetail> {
        return try {
            val response = searchApiService.getRecipeDetails(id)
            return if(response.isSuccessful) {
                response.body()?.meals?.let {
                    if (it.isNotEmpty()) {
                        Result.success(it.first().toDomain())
                    } else {
                        Result.failure(Exception("No recipe details found"))
                    }
                } ?: run { Result.failure(Exception("No recipe details found")) }
            } else {
                Result.failure(Exception("Failed to fetch recipe details"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun insertRecipe(recipe: Recipe) {
        recipeDao.insertRecipe(recipe)
    }

    override suspend fun deleteRecipe(recipe: Recipe) {
        recipeDao.deleteRecipe(recipe)
    }

    override fun getAllRecipes(): Flow<List<Recipe>> {
        return recipeDao.getAllRecipes()
    }
}
