package com.reyaz.searchrecipeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.reyaz.searchrecipeapp.navigation.NavigationSubGraphs
import com.reyaz.searchrecipeapp.navigation.RecipeNavigation
import com.reyaz.searchrecipeapp.ui.theme.SearchRecipeAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigationSubGraphs: NavigationSubGraphs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SearchRecipeAppTheme {
                Surface(
                    modifier = Modifier.safeContentPadding()
                ){
                    RecipeNavigation(navigationSubGraphs = navigationSubGraphs)
                }
            }
        }
    }
}
