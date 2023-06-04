package io.groovin.expandablebox

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.SwipeableDefaults
import androidx.compose.material.SwipeableState
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.Velocity

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ExpandableBox(
    modifier: Modifier = Modifier,
    isDownDirection: Boolean = true,
    swipeableState: SwipeableState<ExpandableBoxState> = rememberSwipeableState(initialValue = ExpandableBoxState.FOLD),
    foldHeight: Dp,
    isHideable: Boolean = true,
    content: @Composable ExpandableBoxScope.() -> Unit
) {
    BoxWithConstraints(
        modifier = modifier
    ) {
        val density = LocalDensity.current
        val foldHeightPx = with(density) { foldHeight.toPx() }
        val maxHeightPx = with(density) { maxHeight.toPx() }
        val anchors =
            if (isHideable) {
                mapOf(
                    0f to ExpandableBoxState.HIDE,
                    foldHeightPx to ExpandableBoxState.FOLD,
                    maxHeightPx to ExpandableBoxState.EXPAND
                )
            } else {
                mapOf(
                    foldHeightPx to ExpandableBoxState.FOLD,
                    maxHeightPx to ExpandableBoxState.EXPAND
                )
            }

        val nestedScrollConnection = remember {
            object : NestedScrollConnection {
                override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                    val delta = available.y
                    return if (delta < 0) {
                        Offset(0f, -swipeableState.performDrag(-delta))
                    } else {
                        super.onPreScroll(available, source)
                    }
                }

                override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset {
                    val delta = available.y
                    val progressState = swipeableState.getProgressState(foldHeightPx, maxHeightPx)
                    return if (source == NestedScrollSource.Drag || progressState != ExpandableBoxState.EXPAND) {
                        Offset(0f, -swipeableState.performDrag(-delta))
                    } else {
                        super.onPostScroll(consumed, available, source)
                    }
                }

                override suspend fun onPostFling(
                    consumed: Velocity,
                    available: Velocity
                ): Velocity {
                    swipeableState.performFling(velocity = available.y)
                    return super.onPostFling(consumed, available)
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .swipeable(
                    state = swipeableState,
                    anchors = anchors,
                    enabled = swipeableState.currentValue != ExpandableBoxState.HIDE,
                    orientation = Orientation.Vertical,
                    reverseDirection = isDownDirection,
                    thresholds = { _, _ -> FractionalThreshold(0.7f) },
                    resistance = SwipeableDefaults.resistanceConfig(anchors.keys, 0f, 0f) //Prevent moving animation when over swiping
                )
                .nestedScroll(nestedScrollConnection)
        ) {
            val innerHeightDp = with(density) { (swipeableState.offset.value).toDp() }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(innerHeightDp)
            ) {
                val progressState = swipeableState.getProgressState(foldHeightPx, maxHeightPx)
                val progress = if (progressState == ExpandableBoxState.HIDE || progressState == ExpandableBoxState.HIDING) {
                    if (foldHeightPx != 0f) {
                        (swipeableState.offset.value / (foldHeightPx)).coerceIn(0f, 1f)
                    } else {
                        1f
                    }
                } else {
                    if (maxHeightPx != foldHeightPx) {
                        ((swipeableState.offset.value - foldHeightPx) / (maxHeightPx - foldHeightPx)).coerceIn(0f, 1f)
                    } else {
                        1f
                    }
                }
                ExpandableBoxScope(
                    progress,
                    progressState,
                    swipeableState.currentValue,
                    this
                ).content()
            }
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
private fun SwipeableState<ExpandableBoxState>.getProgressState(foldHeightPx: Float, maxHeightPx: Float): ExpandableBoxState {
    return when {
        offset.value <= 0 -> ExpandableBoxState.HIDE
        offset.value < foldHeightPx -> ExpandableBoxState.HIDING
        offset.value == foldHeightPx -> ExpandableBoxState.FOLD
        offset.value > foldHeightPx && offset.value < maxHeightPx -> ExpandableBoxState.FOLDING
        else -> ExpandableBoxState.EXPAND
    }
}