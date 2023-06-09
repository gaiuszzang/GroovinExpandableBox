package io.groovin.expandablebox.sampleapp.music

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import io.groovin.expandablebox.ExpandableBox
import io.groovin.expandablebox.ExpandableBoxState
import io.groovin.expandablebox.sampleapp.ui.theme.Pink80
import kotlinx.coroutines.launch

private val MusicPlayerScreenBackgroundColor = Pink80

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MusicSampleScreen(
    isUpside: Boolean
) {
    val coroutineScope = rememberCoroutineScope()
    var selectedItemIndex by remember { mutableIntStateOf(0) }
    val context = LocalContext.current
    val foldHeight = remember { 100.dp }
    Box(modifier = Modifier.fillMaxSize()) {
        val swipeableState = rememberSwipeableState(
            initialValue = ExpandableBoxState.FOLD
        )
        MusicListScreen(
            onItemClick = { index ->
                selectedItemIndex = index
                if (swipeableState.currentValue == ExpandableBoxState.HIDE) {
                    coroutineScope.launch {
                        swipeableState.animateTo(ExpandableBoxState.FOLD)
                    }
                }
            }
        )
        ExpandableBox(
            modifier = Modifier
                .align(if (isUpside) Alignment.TopCenter else Alignment.BottomCenter)
                .fillMaxWidth(),
            swipeableState = swipeableState,
            isDownDirection = !isUpside,
            foldHeight = foldHeight
        ) {
            MusicPlayerScreen(
                modifier = Modifier.background(MusicPlayerScreenBackgroundColor),
                selectedItemIndex = selectedItemIndex,
                progress = progress,
                progressState = progressState,
                foldHeight = foldHeight,
                isUpside = isUpside,
                foldClick = {
                    coroutineScope.launch {
                        swipeableState.animateTo(ExpandableBoxState.FOLD)
                    }
                },
                playClick = {
                    Toast.makeText(context, "Play it!", Toast.LENGTH_SHORT).show()
                }
            )
            SyncStatusBarColor(progressState, colorScheme.background)
            BackHandler(
                enabled = (completedState == ExpandableBoxState.EXPAND)
            ) {
                coroutineScope.launch {
                    swipeableState.animateTo(ExpandableBoxState.FOLD)
                }
            }
        }
    }
}

@Composable
private fun SyncStatusBarColor(
    expandableBoxState: ExpandableBoxState,
    defaultColor: Color,
    expandColor: Color = MusicPlayerScreenBackgroundColor
) {
    val view = LocalView.current
    LaunchedEffect(expandableBoxState) {
        val window = (view.context as Activity).window
        if (expandableBoxState == ExpandableBoxState.EXPAND) {
            window.statusBarColor = expandColor.toArgb()
        } else {
            window.statusBarColor = defaultColor.toArgb()
        }
    }
}