package com.reyaz.search.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.reyaz.common.navigation.FeatureApi
import com.reyaz.common.navigation.NavigationRoute
import com.reyaz.common.navigation.NavigationSubGraphRoute
import com.reyaz.search.domain.model.RecipeDetail
import com.reyaz.search.screens.details.RecipeDetails
import com.reyaz.search.screens.details.RecipeDetailsScreen
import com.reyaz.search.screens.details.RecipeDetailsViewModel
import com.reyaz.search.screens.favorite.FavoriteScreen
import com.reyaz.search.screens.favorite.FavoriteViewModel
import com.reyaz.search.screens.recipe_list.RecipeList
import com.reyaz.search.screens.recipe_list.RecipeListScreen
import com.reyaz.search.screens.recipe_list.RecipeListViewModel

interface SearchFeatureApi : FeatureApi {

}

class SearchFeatureApiImpl : SearchFeatureApi {
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navHostController: NavHostController
    ) {
        navGraphBuilder.navigation(
            route = NavigationSubGraphRoute.Search.route,
            startDestination = NavigationRoute.RecipeList.route
        ) {
            composable(route = NavigationRoute.RecipeList.route) {
                val viewModel = hiltViewModel<RecipeListViewModel>()
                RecipeListScreen(
                    viewModel = viewModel,
                    onClick = { viewModel.onEvent(RecipeList.Event.GoToRecipeDetails(it)) },
                    navHostController = navHostController
                    )
            }
            composable(route = NavigationRoute.RecipeDetails.route) {
                val viewModel = hiltViewModel<RecipeDetailsViewModel>()
                val mealId = it.arguments?.getString("id")
                LaunchedEffect(key1 = mealId) {
                    mealId?.let { id ->
                        viewModel.onEvent(RecipeDetails.Event.FetchRecipeDetails(id))
                    }
                }
                RecipeDetailsScreen(
                    viewModel = viewModel,
                    onNavigationClick = { navHostController.popBackStack() },
                    onDelete = {
                        viewModel.onEvent(RecipeDetails.Event.DeleteRecipe(it))
                    },
                    onFavoriteClick = {
                        viewModel.onEvent(RecipeDetails.Event.InsertRecipe(it))
                    },
                    navHostController = navHostController
                )
            }
            composable(route = NavigationRoute.Favorite.route) {
                val viewModel = hiltViewModel<FavoriteViewModel>()
                FavoriteScreen(
                    viewModel = viewModel,
                    onClick = { viewModel.onEvent(FavoriteScreen.Event.GoToDetails(it)) },
                      navHostController = navHostController,
                )
            }

        }
    }
}