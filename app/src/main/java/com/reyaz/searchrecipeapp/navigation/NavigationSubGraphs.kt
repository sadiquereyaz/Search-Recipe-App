package com.reyaz.searchrecipeapp.navigation

import com.reyaz.search.navigation.SearchFeatureApi
import gaur.himanshu.media_player.navigation.MediaPlayerFeatureAPi

data class NavigationSubGraphs(
    val searchFeatureApi: SearchFeatureApi,
    val mediaPlayerFeatureApi: MediaPlayerFeatureAPi
)