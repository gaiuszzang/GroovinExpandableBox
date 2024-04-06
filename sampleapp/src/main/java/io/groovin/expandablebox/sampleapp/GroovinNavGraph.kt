package io.groovin.expandablebox.sampleapp

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import io.groovin.expandablebox.sampleapp.article.ArticleSampleScreen
import io.groovin.expandablebox.sampleapp.map.MapNestedScrollOption
import io.groovin.expandablebox.sampleapp.map.MapSampleScreen
import io.groovin.expandablebox.sampleapp.music.MusicSampleScreen


object GroovinDestination {
    const val MAIN = "Main"
    const val MUSIC_SAMPLE = "MusicSample"
    const val ARTICLE_SAMPLE = "ArticleSample"
    const val MAP_SAMPLE = "MapSample"
}

val LocalNavAction = compositionLocalOf<GroovinAction> { error("can't find GroovinAction") }

@Composable
fun GroovinNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = GroovinDestination.MAIN,
        enterTransition = {
            fadeIn(animationSpec = tween(700)) + slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Up,
                animationSpec = tween(500)
            )
        },
        exitTransition = {
            fadeOut(animationSpec = tween(700))
        },
        popEnterTransition = {
            fadeIn(animationSpec = tween(700))
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(700)) + slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Down,
                animationSpec = tween(700)
            )
        }
    ) {
        composable(GroovinDestination.MAIN) {
            MainScreen()
        }
        composable(GroovinDestination.MUSIC_SAMPLE) {
            MusicSampleScreen()
        }
        composable(GroovinDestination.ARTICLE_SAMPLE) {
            ArticleSampleScreen()
        }
        composable(
            route = "${GroovinDestination.MAP_SAMPLE}?" +
                "nestedScrollOption={nestedScrollOption}",
            arguments = listOf(
                navArgument("nestedScrollOption") { defaultValue = 0 }
            )
        ) {
            val nestedScrollOption = it.arguments?.getInt("nestedScrollOption", 0) ?: 0
            MapSampleScreen(MapNestedScrollOption.from(nestedScrollOption))
        }
    }
}

class GroovinAction(private val navController: NavHostController?) {
    val moveToMusicBottomExpandBox: () -> Unit = {
        navController?.navigate(GroovinDestination.MUSIC_SAMPLE)
    }
    val moveToArticleExpandBox: () -> Unit = {
        navController?.navigate(GroovinDestination.ARTICLE_SAMPLE)
    }
    val moveToMapExpandBox: (Int) -> Unit = { nestedScrollOption ->
        navController?.navigate(GroovinDestination.MAP_SAMPLE + "?nestedScrollOption=$nestedScrollOption")
    }
    val moveToBack: () -> Unit = {
        navController?.popBackStack()
    }
}

