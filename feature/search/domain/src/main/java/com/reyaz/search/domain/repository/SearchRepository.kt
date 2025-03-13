package com.reyaz.search.domain.repository

import com.reyaz.search.domain.model.Recipe
import com.reyaz.search.domain.model.RecipeDetail
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    suspend fun getRecipes(s: String): Result<List<Recipe>>
    suspend fun getRecipeDetails(id: String): Result<RecipeDetail>
    suspend fun insertRecipe(recipe: Recipe)
    suspend fun deleteRecipe(recipe: Recipe)
    fun getAllRecipes(): Flow<List<Recipe>>
}