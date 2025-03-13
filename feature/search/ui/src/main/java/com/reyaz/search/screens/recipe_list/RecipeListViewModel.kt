package com.reyaz.search.screens.recipe_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewModelScope
import com.reyaz.common.utils.NetworkResult
import com.reyaz.common.utils.UiText
import com.reyaz.search.domain.model.Recipe
import com.reyaz.search.domain.model.RecipeDetail
import com.reyaz.search.domain.use_cases.DeleteRecipeUseCase
import com.reyaz.search.domain.use_cases.GetAllRecipeFromLocalDbUseCase
import com.reyaz.search.domain.use_cases.GetAllRecipeUseCase
import com.reyaz.search.domain.use_cases.InsertRecipeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeListViewModel @Inject constructor(
    private val getAllRecipeUseCase: GetAllRecipeUseCase
) : ViewModel() {
    /**
     * This property exposes the [_uiState] as a read-only [StateFlow].
     * Components observing this [StateFlow] will be automatically updated
     * whenever the UI state changes.
     */
    private val _uiState = MutableStateFlow(RecipeList.UiState())
    val uiState: StateFlow<RecipeList.UiState> get() = _uiState.asStateFlow()

    private val  _navigation = Channel<RecipeList.Navigation>()
    val navigation: Flow<RecipeList.Navigation> = _navigation.receiveAsFlow()    // hot flow

    fun onEvent(event: RecipeList.Event) {
        when (event) {
            is RecipeList.Event.SearchRecipe -> search(event.q)
            is RecipeList.Event.GoToRecipeDetails -> {
                viewModelScope.launch {
                    _navigation.send(
                        RecipeList.Navigation.GoToRecipeDetails(event.id)
                    )
                }
            }

            RecipeList.Event.FavoriteScreen -> viewModelScope.launch{
                _navigation.send(RecipeList.Navigation.FavoriteScreen)
            }
        }
    }

    private fun search(q: String) =
        getAllRecipeUseCase.invoke(q)
            .onEach { result ->
                when (result) {
                    is NetworkResult.Loading -> {
                        _uiState.update { RecipeList.UiState(isLoading = true) }
                    }

                    is NetworkResult.Error -> {
                        _uiState.update {
                            RecipeList.UiState(
                                isLoading = false,
                                error = UiText.RemoteString(result.message ?: "Unknown error")
                            )
                        }
                    }

                    is NetworkResult.Success -> {
                        _uiState.update { it.copy(isLoading = false, data = result.data) }
                    }
                }
            }.launchIn(viewModelScope)

}

object RecipeList {

    data class UiState(
        val isLoading: Boolean = false,
        val data: List<Recipe>? = null,
        val error: UiText? = UiText.Idle
    )

    sealed interface Navigation {
        data class GoToRecipeDetails(val id: String) : Navigation
        data object FavoriteScreen : Navigation
    }

    sealed interface Event {
        data class SearchRecipe(val q: String = "c") : Event
        data class GoToRecipeDetails(val id: String) : Event
        data object FavoriteScreen : Event
        }

}