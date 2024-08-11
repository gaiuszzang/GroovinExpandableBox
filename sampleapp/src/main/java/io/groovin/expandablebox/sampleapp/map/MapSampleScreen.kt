package io.groovin.expandablebox.sampleapp.map

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import io.groovin.expandablebox.ExpandableBox
import io.groovin.expandablebox.ExpandableBoxStateValue
import io.groovin.expandablebox.ExpandableBoxSwipeDirection
import io.groovin.expandablebox.rememberExpandableBoxState
import io.groovin.expandablebox.sampleapp.R
import io.groovin.expandablebox.sampleapp.ui.theme.Purple40
import kotlinx.coroutines.launch

@Composable
fun MapSampleScreen(
    nestedScrollOption: MapNestedScrollOption
) {
    val coroutineScope = rememberCoroutineScope()
    val hideHeight = remember { 96.dp }
    val foldHeight = remember { 300.dp }
    Box(modifier = Modifier.fillMaxSize()) {
        val contentScrollState = rememberScrollState()
        val expandableBoxState = rememberExpandableBoxState(
            initialValue = ExpandableBoxStateValue.HalfExpand,
            confirmStateChange = { newValue ->
                if (nestedScrollOption != MapNestedScrollOption.Disable && newValue != ExpandableBoxStateValue.Expand) {
                    coroutineScope.launch {
                        contentScrollState.scrollTo(0)
                    }
                }
                true
            }
        )
        // sample map image
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.img_sample_map),
            contentScale = ContentScale.Crop,
            contentDescription = null
        )
        // Search Bar, Location Button UI
        val extraUiVisibility by remember {
            derivedStateOf {
                (expandableBoxState.progressValue != ExpandableBoxStateValue.Expanding) &&
                (expandableBoxState.progressValue != ExpandableBoxStateValue.Expand)
            }
        }
        var searchText by remember { mutableStateOf("") }
        AnimatedVisibility(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.TopCenter),
            visible = extraUiVisibility,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            BasicTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(elevation = 5.dp, shape = RoundedCornerShape(10.dp))
                    .background(color = Color.White, shape = RoundedCornerShape(10.dp))
                    .padding(16.dp),
                value = searchText,
                onValueChange = { searchText = it }
            )
        }

        val density = LocalDensity.current
        AnimatedVisibility(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .padding(bottom = (with(density) { expandableBoxState.offset.value.toDp() }).coerceAtMost(foldHeight)),
            visible = extraUiVisibility,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            FloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        expandableBoxState.animateTo(ExpandableBoxStateValue.Fold)
                    }
                },
                containerColor = Purple40,
                elevation = FloatingActionButtonDefaults.elevation(0.dp,0.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.LocationOn,
                    tint = Color.White,
                    contentDescription = null
                )
            }
        }

        val nestedScrollEnabled by remember {
            derivedStateOf {
                when (nestedScrollOption) {
                    MapNestedScrollOption.Use -> true
                    MapNestedScrollOption.Disable -> false
                    MapNestedScrollOption.ConditionalUse -> {
                        expandableBoxState.completedValue != ExpandableBoxStateValue.Expand
                    }
                }
            }
        }
        // Expandable Box
        ExpandableBox(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .testTag("MapBottomSheetExpandableBox"),
            expandableBoxState = expandableBoxState,
            swipeDirection = ExpandableBoxSwipeDirection.SwipeUpToExpand,
            foldHeight = hideHeight,
            halfExpandHeight = foldHeight,
            nestedScrollEnabled = nestedScrollEnabled
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .conditional(condition = progressState != ExpandableBoxStateValue.Expand) {
                        shadow(elevation = 5.dp, shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    }
                    .background(color = Color.White)
            ) {
                Icon(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(12.dp),
                    imageVector = Icons.Default.Menu,
                    contentDescription = null
                )
                MapBottomSheetScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    scrollState = contentScrollState
                )
            }
            BackHandler(
                enabled = (completedState != ExpandableBoxStateValue.Fold)
            ) {
                coroutineScope.launch {
                    contentScrollState.scrollTo(0)
                    if (completedState == ExpandableBoxStateValue.Expand) {
                        expandableBoxState.animateTo(ExpandableBoxStateValue.HalfExpand)
                    } else {
                        expandableBoxState.animateTo(ExpandableBoxStateValue.Fold)
                    }
                }
            }
        }
    }
}

enum class MapNestedScrollOption {
    Use, ConditionalUse, Disable;

    companion object {
        fun from(value: Int): MapNestedScrollOption {
            return when (value) {
                0 -> Use
                1 -> ConditionalUse
                2 -> Disable
                else -> Use
            }
        }
    }
}

private inline fun Modifier.conditional(
    condition: Boolean,
    ifTrue: Modifier.() -> Modifier,
): Modifier = if (condition) {
    then(ifTrue(Modifier))
} else {
    then(this)
}
