package com.reyaz.search.screens.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reyaz.common.utils.UiText
import com.reyaz.search.domain.model.Recipe
import com.reyaz.search.domain.use_cases.DeleteRecipeUseCase
import com.reyaz.search.domain.use_cases.GetAllRecipeFromLocalDbUseCase
import com.reyaz.search.screens.details.RecipeDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val getAllRecipeFromLocalDbUseCase: GetAllRecipeFromLocalDbUseCase,
    private val deleteRecipeUseCase: DeleteRecipeUseCase
) :
    ViewModel() {
    private val _uiState = MutableStateFlow(FavoriteScreen.UiState())
    val uiState: MutableStateFlow<FavoriteScreen.UiState> get() = _uiState

    private val _navigation = Channel<FavoriteScreen.Navigation>()
    val navigation: Flow<FavoriteScreen.Navigation> get() = _navigation.receiveAsFlow()

    private var originalList = mutableListOf<Recipe>()

    init {
        getAllFavoriteRecipes()
    }

    fun onEvent(event: FavoriteScreen.Event) {
        when (event) {
            is FavoriteScreen.Event.AlphabeticalSort -> alphabeticalSort()
            is FavoriteScreen.Event.LessIngredientsSort -> lessIngredientsSort()
            is FavoriteScreen.Event.ResetSort -> resetSort()
            is FavoriteScreen.Event.ShowDetails -> viewModelScope.launch {
                _navigation.send(FavoriteScreen.Navigation.GoToDetails(event.id))
            }
            is FavoriteScreen.Event.DeleteRecipe -> deleteRecipe(event.recipe)
            is FavoriteScreen.Event.GoToDetails -> _navigation.trySend(FavoriteScreen.Navigation.GoToDetails(event.id))
        }
    }

    private fun getAllFavoriteRecipes() {
        viewModelScope.launch {
            getAllRecipeFromLocalDbUseCase.invoke().collect { list ->
                originalList = list.toMutableList()
                _uiState.update {
                    FavoriteScreen.UiState(data = list)
                }
            }
        }

    }

    private fun alphabeticalSort() =
        _uiState.update { FavoriteScreen.UiState(data = originalList.sortedBy { it.strMeal }) }

    private fun lessIngredientsSort() =
        _uiState.update { FavoriteScreen.UiState(data = originalList.sortedBy { it.strInstructions.length }) }

    private fun resetSort() = _uiState.update { FavoriteScreen.UiState(data = originalList) }

    private fun deleteRecipe(recipe: Recipe) = deleteRecipeUseCase.invoke(recipe).launchIn(viewModelScope)
}

object FavoriteScreen {
    data class UiState(
        val isLoading: Boolean = false,
        val data: List<Recipe>? = emptyList(),
        val error: UiText? = UiText.Idle
    )

    sealed interface Event {
        data object AlphabeticalSort : Event
        data object LessIngredientsSort : Event
        data object ResetSort : Event
        data class ShowDetails(val id: String) : Event
        data class DeleteRecipe(val recipe: Recipe) : Event
        data class GoToDetails(val id:String) : Event
    }

    sealed interface Navigation {
        data class GoToDetails(val id: String) : Navigation
    }
}