package com.reyaz.search.domain.use_cases

import com.reyaz.search.domain.model.Recipe
import com.reyaz.search.domain.repository.SearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class InsertRecipeUseCase @Inject constructor( private val searchRepository: SearchRepository) {
    operator fun invoke(recipe: Recipe) = flow<Unit> {
        searchRepository.insertRecipe(recipe)
    }.flowOn(Dispatchers.IO)    // execute this flow on io thread
}