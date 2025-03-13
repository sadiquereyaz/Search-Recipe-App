package com.reyaz.common.navigation

sealed class NavigationRoute(val route: String) {
    data object RecipeList : NavigationRoute("/recipe_list")
    data object RecipeDetails : NavigationRoute("/recipe_details/{id}") {
        fun sendId(id: String) = "/recipe_details/${id}"
    }
    data object Favorite : NavigationRoute("/favorite")
    data object MediaPlayer : NavigationRoute("/player/{videoId}") {
        fun sendUrl (videoId: String) = "/player/$videoId"
    }
}

sealed class NavigationSubGraphRoute(val route: String) {
    data object Search: NavigationSubGraphRoute("/search")
    data object MediaPlayer: NavigationSubGraphRoute("/media_player")
}