package io.groovin.expandablebox

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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

@Composable
fun ExpandableBox(
    modifier: Modifier = Modifier,
    swipeDirection: ExpandableBoxSwipeDirection = ExpandableBoxSwipeDirection.SwipeUpToExpand,
    expandableBoxState: ExpandableBoxState = rememberExpandableBoxState(initialValue = ExpandableBoxStateValue.Fold),
    foldHeight: Dp,
    halfExpandHeight: Dp = foldHeight,
    expandHeight: Dp = Dp.Unspecified,
    content: @Composable ExpandableBoxScope.() -> Unit
) {
    BoxWithConstraints(
        modifier = modifier
    ) {
        val density = LocalDensity.current
        val expandHeightPx = with(density) { if (expandHeight != Dp.Unspecified) expandHeight.toPx() else maxHeight.toPx() }
        val foldHeightPx = with(density) { foldHeight.toPx().coerceAtMost(expandHeightPx) }
        val halfExpandHeightPx = with(density) { halfExpandHeight.toPx().coerceIn(foldHeightPx, expandHeightPx) }
        val anchors = if (foldHeight == halfExpandHeight) {
            mapOf(
                foldHeightPx to ExpandableBoxStateValue.Fold,
                expandHeightPx to ExpandableBoxStateValue.Expand
            )
        } else {
            mapOf(
                foldHeightPx to ExpandableBoxStateValue.Fold,
                halfExpandHeightPx to ExpandableBoxStateValue.HalfExpand,
                expandHeightPx to ExpandableBoxStateValue.Expand
            )
        }

        val nestedScrollConnection = remember {
            object : NestedScrollConnection {
                override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                    val delta = available.y
                    return if (delta < 0) {
                        Offset(0f, -expandableBoxState.performDrag(-delta))
                    } else {
                        super.onPreScroll(available, source)
                    }
                }

                override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset {
                    val delta = available.y
                    val progressState = expandableBoxState.progressValue
                    return if (source == NestedScrollSource.Drag || progressState != ExpandableBoxStateValue.Expand) {
                        Offset(0f, -expandableBoxState.performDrag(-delta))
                    } else {
                        super.onPostScroll(consumed, available, source)
                    }
                }

                override suspend fun onPostFling(
                    consumed: Velocity,
                    available: Velocity
                ): Velocity {
                    expandableBoxState.performFling(velocity = available.y)
                    return super.onPostFling(consumed, available)
                }
            }
        }
        val isDownDirection = remember(swipeDirection) { swipeDirection == ExpandableBoxSwipeDirection.SwipeUpToExpand }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .expandableBoxSwipeable(
                    state = expandableBoxState,
                    anchors = anchors,
                    enabled = foldHeightPx > 0 || expandableBoxState.completedValue != ExpandableBoxStateValue.Fold,
                    orientation = Orientation.Vertical,
                    reverseDirection = isDownDirection,
                    thresholds = { _, _ -> FractionalThreshold(0.7f) },
                    resistance = ExpandableBoxSwipeableDefaults.resistanceConfig(anchors.keys, 0f, 0f) //Prevent moving animation when over swiping
                )
                .nestedScroll(nestedScrollConnection)
        ) {
            val innerHeightDp = with(density) { (expandableBoxState.offset.value).toDp() }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(innerHeightDp)
            ) {
                expandableBoxState.updateProgressState(foldHeightPx, halfExpandHeightPx, expandHeightPx)
                val progressState = expandableBoxState.progressValue
                val progress = if (progressState == ExpandableBoxStateValue.Fold || progressState == ExpandableBoxStateValue.Folding) {
                    if (halfExpandHeightPx != 0f) {
                        (expandableBoxState.offset.value / (halfExpandHeightPx)).coerceIn(0f, 1f)
                    } else {
                        1f
                    }
                } else {
                    if (expandHeightPx != halfExpandHeightPx) {
                        ((expandableBoxState.offset.value - halfExpandHeightPx) / (expandHeightPx - halfExpandHeightPx)).coerceIn(0f, 1f)
                    } else {
                        1f
                    }
                }
                ExpandableBoxScope(
                    progress,
                    progressState,
                    expandableBoxState.completedValue,
                    this
                ).content()
            }
        }
    }
}

private fun ExpandableBoxState.updateProgressState(foldHeightPx: Float, halfExpandHeightPx: Float, expandHeightPx: Float) {
    progressValue = if (foldHeightPx == halfExpandHeightPx) {
        when {
            offset.value <= foldHeightPx -> ExpandableBoxStateValue.Fold
            offset.value > foldHeightPx && offset.value < expandHeightPx -> ExpandableBoxStateValue.Expanding
            else -> ExpandableBoxStateValue.Expand
        }
    } else {
        when {
            offset.value <= foldHeightPx -> ExpandableBoxStateValue.Fold
            offset.value < halfExpandHeightPx -> ExpandableBoxStateValue.Folding
            offset.value == halfExpandHeightPx -> ExpandableBoxStateValue.HalfExpand
            offset.value > halfExpandHeightPx && offset.value < expandHeightPx -> ExpandableBoxStateValue.Expanding
            else -> ExpandableBoxStateValue.Expand
        }
    }
}