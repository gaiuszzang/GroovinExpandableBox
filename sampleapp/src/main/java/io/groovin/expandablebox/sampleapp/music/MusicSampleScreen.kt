package io.groovin.expandablebox.sampleapp.music

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import io.groovin.expandablebox.ExpandableBox
import io.groovin.expandablebox.ExpandableBoxStateValue
import io.groovin.expandablebox.ExpandableBoxSwipeDirection
import io.groovin.expandablebox.rememberExpandableBoxState
import io.groovin.expandablebox.sampleapp.ui.theme.Pink80
import kotlinx.coroutines.launch

private val MusicPlayerScreenBackgroundColor = Pink80

@Composable
fun MusicSampleScreen() {
    val coroutineScope = rememberCoroutineScope()
    var selectedItemIndex by remember { mutableIntStateOf(0) }
    val context = LocalContext.current
    val playerCompactHeight = remember { 64.dp }
    val bottomBarHeight = remember { 80.dp }
    val navigationBarPaddingValues = WindowInsets.navigationBars.asPaddingValues()
    val systemBarsPaddingValues = WindowInsets.systemBars.asPaddingValues()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val expandableBoxState = rememberExpandableBoxState(
            initialValue = ExpandableBoxStateValue.HalfExpand
        )
        val expandableProgress = remember {
            mutableFloatStateOf(1f)
        }
        MusicListScreen(
            modifier = Modifier.padding(systemBarsPaddingValues),
            contentPadding = PaddingValues(bottom = playerCompactHeight + bottomBarHeight),
            onItemClick = { index ->
                selectedItemIndex = index
                if (expandableBoxState.completedValue == ExpandableBoxStateValue.Fold) {
                    coroutineScope.launch {
                        expandableBoxState.animateTo(ExpandableBoxStateValue.HalfExpand)
                    }
                }
            }
        )
        Column(modifier = Modifier.align(Alignment.BottomCenter)) {
            ExpandableBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("MusicExpandableBox"),
                expandableBoxState = expandableBoxState,
                swipeDirection = ExpandableBoxSwipeDirection.SwipeUpToExpand,
                foldHeight = 0.dp,
                halfExpandHeight = playerCompactHeight
            ) {
                expandableProgress.floatValue = this.progress
                MusicPlayerScreen(
                    modifier = Modifier
                        .background(MusicPlayerScreenBackgroundColor),
                    selectedItemIndex = selectedItemIndex,
                    progress = progress,
                    progressState = progressState,
                    minimizedHeight = playerCompactHeight,
                    foldClick = {
                        coroutineScope.launch {
                            expandableBoxState.animateTo(ExpandableBoxStateValue.HalfExpand)
                        }
                    },
                    playClick = {
                        Toast.makeText(context, "Play it!", Toast.LENGTH_SHORT).show()
                    }
                )
                BackHandler(
                    enabled = (completedState == ExpandableBoxStateValue.Expand) || (expandableBoxState.isAnimationRunning)
                ) {
                    if (expandableBoxState.isAnimationRunning) return@BackHandler
                    coroutineScope.launch {
                        expandableBoxState.animateTo(ExpandableBoxStateValue.HalfExpand)
                    }
                }
            }
            val bottomBarDynamicHeight by remember(expandableBoxState.progressValue, expandableProgress.floatValue) {
                derivedStateOf {
                    when (expandableBoxState.progressValue) {
                        ExpandableBoxStateValue.Expand -> 0.dp
                        ExpandableBoxStateValue.Expanding -> bottomBarHeight * (1f - expandableProgress.floatValue)
                        else -> bottomBarHeight
                    }
                }
            }
            Box(
                modifier = Modifier
                    .background(MusicPlayerScreenBackgroundColor)
                    .height(bottomBarDynamicHeight + navigationBarPaddingValues.calculateBottomPadding())
            ) {
                MusicBottomBar(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxWidth()
                        .wrapContentHeight(align = Alignment.Top, unbounded = true)
                        .height(bottomBarHeight)
                )
            }
        }
    }
}
