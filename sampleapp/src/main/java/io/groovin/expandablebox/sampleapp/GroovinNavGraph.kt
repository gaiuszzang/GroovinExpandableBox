package io.groovin.expandablebox.sampleapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.groovin.expandablebox.sampleapp.article.ArticleSampleScreen
import io.groovin.expandablebox.sampleapp.music.MusicSampleScreen

object GroovinDestination {
    const val Main = "Main"
    const val MusicBottomExpandBox = "MusicBottomExpandBox"
    const val MusicUpExpandBox     = "MusicUpExpandBox"
    const val ArticleExpandBox     = "ArticleExpandBox"
}

val LocalNavAction = compositionLocalOf<GroovinAction> { error("can't find GroovinAction") }

@Composable
fun GroovinNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = GroovinDestination.Main) {
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
    val moveToBack: () -> Unit = {
        navController?.popBackStack()
    }
}

