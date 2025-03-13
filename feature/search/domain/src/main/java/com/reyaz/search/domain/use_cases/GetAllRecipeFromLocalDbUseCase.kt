package com.reyaz.search.domain.use_cases

import com.reyaz.search.domain.repository.SearchRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAllRecipeFromLocalDbUseCase @Inject constructor(private val searchRepository: SearchRepository) {
    operator fun invoke() = searchRepository.getAllRecipes()
}