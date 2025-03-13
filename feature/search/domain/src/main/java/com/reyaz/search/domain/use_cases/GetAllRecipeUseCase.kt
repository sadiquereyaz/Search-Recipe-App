package com.reyaz.search.domain.use_cases

import com.reyaz.common.utils.NetworkResult
import com.reyaz.search.domain.model.Recipe
import com.reyaz.search.domain.repository.SearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetAllRecipeUseCase @Inject constructor(private val searchRepository: SearchRepository) {
    /**
     * Executes a search for recipes based on the given query string.
     *
     * This function initiates a network request to retrieve a list of recipes matching the provided
     * query. It emits a [Flow] of [NetworkResult] to handle different states of the network operation:
     * loading, success, or error.
     *
     * @param q The search query string.
     * @return A [Flow] emitting [NetworkResult] objects, which can be one of the following:
     *         - [NetworkResult.Loading]: Emitted when the network request is initiated.
     *         - [NetworkResult.Success]: Emitted when the network request is successful, containing the
     *                                    list of [Recipe] objects.
     *         - [NetworkResult.Error]: Emitted when the network request fails, containing an error message.
     *
     * The function handles potential exceptions during the network operation and emits an error state
     * in such cases. The entire flow is executed on the [Dispatchers.IO] thread to prevent blocking the
     * main thread.
     *
     * Example Usage:
     * ```
     *  someViewModelScope.launch {
     *      invoke("pizza").collect { result ->
     *          when(result){
     *              is NetworkResult.Loading -> {
     *                  // show loading indicator
     *              }
     *              is NetworkResult.Success -> {
     *                  val recipes = result.data
     *                  // update ui with recipes list
     *              }
     *              is NetworkResult.Error -> {
     *                  val message = result.message
     *                  // show error message
     *              }
     *          }
     *      }
     *  }
     * ```
     */
    operator fun invoke(q: String): Flow<NetworkResult<List<Recipe>>> = flow {
        emit(NetworkResult.Loading())
        val response = searchRepository.getRecipes(q)
        if (response.isSuccess)
            emit(NetworkResult.Success(data = response.getOrThrow()))
        else
            emit(NetworkResult.Error(message = response.exceptionOrNull()?.localizedMessage))
    }.catch {
        emit(NetworkResult.Error(message = it.message.toString()))
    }.flowOn(Dispatchers.IO)    // execute this flow on io thread
}