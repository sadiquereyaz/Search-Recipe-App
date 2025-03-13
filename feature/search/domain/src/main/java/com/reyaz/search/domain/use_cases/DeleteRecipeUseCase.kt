package com.reyaz.search.domain.use_cases

import com.reyaz.search.domain.model.Recipe
import com.reyaz.search.domain.repository.SearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DeleteRecipeUseCase @Inject constructor(private val searchRepository: SearchRepository) {
     operator fun invoke(recipe: Recipe) = flow<Unit>{ searchRepository.deleteRecipe(recipe) }.flowOn(Dispatchers.IO)
}