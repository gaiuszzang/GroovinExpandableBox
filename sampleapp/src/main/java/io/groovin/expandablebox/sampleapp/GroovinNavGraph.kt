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
    const val Main = "Main"
    const val MusicBottomExpandBox = "MusicBottomExpandBox"
    const val MusicUpExpandBox     = "MusicUpExpandBox"
    const val ArticleExpandBox     = "ArticleExpandBox"
    const val MapExpandBox         = "MapExpandBox"
}

val LocalNavAction = compositionLocalOf<GroovinAction> { error("can't find GroovinAction") }

@Composable
fun GroovinNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = GroovinDestination.Main,
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
        composable(GroovinDestination.Main) {
            MainScreen()
        }
        composable(GroovinDestination.MusicBottomExpandBox) {
            MusicSampleScreen(false)
        }
        composable(GroovinDestination.MusicUpExpandBox) {
            MusicSampleScreen(true)
        }
        composable(GroovinDestination.ArticleExpandBox) {
            ArticleSampleScreen()
        }
        composable(
            route = "${GroovinDestination.MapExpandBox}?" +
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
        navController?.navigate(GroovinDestination.MusicBottomExpandBox)
    }
    val moveToMusicUpExpandBox: () -> Unit = {
        navController?.navigate(GroovinDestination.MusicUpExpandBox)
    }
    val moveToArticleExpandBox: () -> Unit = {
        navController?.navigate(GroovinDestination.ArticleExpandBox)
    }
    val moveToMapExpandBox: (Int) -> Unit = { nestedScrollOption ->
        navController?.navigate(GroovinDestination.MapExpandBox + "?nestedScrollOption=$nestedScrollOption")
    }
    val moveToBack: () -> Unit = {
        navController?.popBackStack()
    }
}

