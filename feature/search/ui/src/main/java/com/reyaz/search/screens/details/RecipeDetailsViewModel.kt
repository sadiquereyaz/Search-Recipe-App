package com.reyaz.search.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.reyaz.common.utils.NetworkResult
import com.reyaz.common.utils.UiText
import com.reyaz.search.domain.model.Recipe
import com.reyaz.search.domain.model.RecipeDetail
import com.reyaz.search.domain.use_cases.DeleteRecipeUseCase
import com.reyaz.search.domain.use_cases.GetAllRecipeFromLocalDbUseCase
import com.reyaz.search.domain.use_cases.GetRecipeDetailsUseCase
import com.reyaz.search.domain.use_cases.InsertRecipeUseCase
import com.reyaz.search.screens.recipe_list.RecipeList.Event
import dagger.hilt.android.lifecycle.HiltViewModel
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
class RecipeDetailsViewModel @Inject constructor(
    private val recipeDetailsUseCase: GetRecipeDetailsUseCase,
    private val insertRecipeUseCase: InsertRecipeUseCase,
    private val deleteRecipeUseCase: DeleteRecipeUseCase,
    private val getAllRecipeFromLocalDbUseCase: GetAllRecipeFromLocalDbUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecipeDetails.UiState())
    val uiState: StateFlow<RecipeDetails.UiState> get() = _uiState.asStateFlow()

    private val _navigation = Channel<RecipeDetails.Navigation>()
    val navigation: Flow<RecipeDetails.Navigation> get() = _navigation.receiveAsFlow()

    fun onEvent(event: RecipeDetails.Event) {
        when (event) {
            is RecipeDetails.Event.FetchRecipeDetails -> recipeDetails(event.id)
            is RecipeDetails.Event.GoToRecipeListScreen -> {
                viewModelScope.launch {
                    _navigation.send(RecipeDetails.Navigation.GoToRecipeListScreen)
                }
            }
            is RecipeDetails.Event.DeleteRecipe -> deleteRecipeUseCase.invoke(event.recipeDetail.toRecipe()).launchIn(viewModelScope)
            is RecipeDetails.Event.InsertRecipe -> insertRecipeUseCase.invoke(event.recipeDetail.toRecipe()).launchIn(viewModelScope)
            is RecipeDetails.Event.GoToMediaPlayer -> viewModelScope.launch {
                _navigation.send(RecipeDetails.Navigation.GoToMediaPlayer(event.youtubeUrl))
            }
        }
    }

    private fun recipeDetails(id: String) =
        recipeDetailsUseCase.invoke(id)
            .onEach { result ->
                when (result) {
                    is NetworkResult.Loading -> {
                        _uiState.update {
                            RecipeDetails.UiState(
                                isLoading = true
                            )
                        }
                    }

                    is NetworkResult.Error -> {
                        _uiState.update {
                            RecipeDetails.UiState(
                                error = UiText.RemoteString(result.message.toString())
                            )
                        }
                    }

                    is NetworkResult.Success -> {
                        _uiState.update {
                            RecipeDetails.UiState(
                                data = result.data
                            )
                        }
                    }
                }
            }.launchIn(viewModelScope)
    private fun RecipeDetail.toRecipe(): Recipe {
        return Recipe(
            idMeal = this.idMeal,
            strArea = this.strArea,
            strMeal = this.strMeal,
            strMealThumb = this.strMealThumb,
            strCategory = this.strCategory,
            strTags = this.strTags,
            strYoutube = this.strYoutube,
            strInstructions = this.strInstructions
        )
    }
}

object RecipeDetails {

    data class UiState(
        val isLoading: Boolean = false,
        val data: RecipeDetail? = null,
        val error: UiText? = UiText.Idle
    )

    sealed interface Navigation {
        data object GoToRecipeListScreen : Navigation
        data class GoToMediaPlayer(val youtubeUrl: String) : Navigation
    }

    sealed interface Event {
        data class FetchRecipeDetails(val id: String) : Event
        data object GoToRecipeListScreen : Event
        data class InsertRecipe(val recipeDetail: RecipeDetail) : Event
        data class DeleteRecipe(val recipeDetail: RecipeDetail) : Event
        data class GoToMediaPlayer(val youtubeUrl: String) : Event
    }

}