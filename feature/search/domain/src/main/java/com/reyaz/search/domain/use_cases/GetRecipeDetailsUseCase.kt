package com.reyaz.search.domain.use_cases

import com.reyaz.common.utils.NetworkResult
import com.reyaz.search.domain.model.RecipeDetail
import com.reyaz.search.domain.repository.SearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetRecipeDetailsUseCase @Inject constructor(private val searchRepository: SearchRepository) {

    operator fun invoke(id: String): Flow<NetworkResult<RecipeDetail>> = flow {
        emit(NetworkResult.Loading())
        val response = searchRepository.getRecipeDetails(id)
        if (response.isSuccess)
            emit(NetworkResult.Success(data = response.getOrThrow()))
        else emit(NetworkResult.Error(message = response.exceptionOrNull()?.localizedMessage))
    }.catch {
        emit(NetworkResult.Error(message = it.message.toString()))
    }.flowOn(Dispatchers.IO)
}