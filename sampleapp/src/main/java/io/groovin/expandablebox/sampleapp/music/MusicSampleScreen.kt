package io.groovin.expandablebox.sampleapp.music

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import io.groovin.expandablebox.ExpandableBoxStateValue
import io.groovin.expandablebox.ExpandableBoxSwipeDirection
import io.groovin.expandablebox.rememberExpandableBoxState
import io.groovin.expandablebox.sampleapp.ui.theme.Pink80
import kotlinx.coroutines.launch

private val MusicPlayerScreenBackgroundColor = Pink80

@Composable
fun MusicSampleScreen(
    isUpside: Boolean
) {
    val coroutineScope = rememberCoroutineScope()
    var selectedItemIndex by remember { mutableIntStateOf(0) }
    val context = LocalContext.current
    val minimizedHeight = remember { 100.dp }
    Box(modifier = Modifier.fillMaxSize()) {
        val swipeableState = rememberExpandableBoxState(
            initialValue = ExpandableBoxStateValue.HalfExpand
        )
        MusicListScreen(
            onItemClick = { index ->
                selectedItemIndex = index
                if (swipeableState.completedValue == ExpandableBoxStateValue.Fold) {
                    coroutineScope.launch {
                        swipeableState.animateTo(ExpandableBoxStateValue.HalfExpand)
                    }
                }
            }
        )
        ExpandableBox(
            modifier = Modifier
                .align(if (isUpside) Alignment.TopCenter else Alignment.BottomCenter)
                .fillMaxWidth(),
            expandableBoxState = swipeableState,
            swipeDirection = if (isUpside) ExpandableBoxSwipeDirection.SwipeDownToExpand else ExpandableBoxSwipeDirection.SwipeUpToExpand,
            foldHeight = 0.dp,
            halfExpandHeight = minimizedHeight
        ) {
            MusicPlayerScreen(
                modifier = Modifier.background(MusicPlayerScreenBackgroundColor),
                selectedItemIndex = selectedItemIndex,
                progress = progress,
                progressState = progressState,
                minimizedHeight = minimizedHeight,
                isUpside = isUpside,
                foldClick = {
                    coroutineScope.launch {
                        swipeableState.animateTo(ExpandableBoxStateValue.HalfExpand)
                    }
                },
                playClick = {
                    Toast.makeText(context, "Play it!", Toast.LENGTH_SHORT).show()
                }
            )
            SyncStatusBarColor(progressState, colorScheme.background)
            BackHandler(
                enabled = (completedState == ExpandableBoxStateValue.Expand)
            ) {
                coroutineScope.launch {
                    swipeableState.animateTo(ExpandableBoxStateValue.HalfExpand)
                }
            }
        }
    }
}

@Composable
private fun SyncStatusBarColor(
    expandableBoxState: ExpandableBoxStateValue,
    defaultColor: Color,
    expandColor: Color = MusicPlayerScreenBackgroundColor
) {
    val view = LocalView.current
    LaunchedEffect(expandableBoxState) {
        val window = (view.context as Activity).window
        if (expandableBoxState == ExpandableBoxStateValue.Expand) {
            window.statusBarColor = expandColor.toArgb()
        } else {
            window.statusBarColor = defaultColor.toArgb()
        }
    }
}