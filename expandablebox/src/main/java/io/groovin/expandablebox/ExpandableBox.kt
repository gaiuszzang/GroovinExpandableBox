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
    isDownDirection: Boolean = true,
    expandableBoxState: ExpandableBoxState = rememberExpandableBoxState(initialValue = ExpandableBoxStateValue.FOLD),
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
                    0f to ExpandableBoxStateValue.HIDE,
                    foldHeightPx to ExpandableBoxStateValue.FOLD,
                    maxHeightPx to ExpandableBoxStateValue.EXPAND
                )
            } else {
                mapOf(
                    foldHeightPx to ExpandableBoxStateValue.FOLD,
                    maxHeightPx to ExpandableBoxStateValue.EXPAND
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
                    val progressState = expandableBoxState.getProgressState(foldHeightPx, maxHeightPx)
                    return if (source == NestedScrollSource.Drag || progressState != ExpandableBoxStateValue.EXPAND) {
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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .expandableBoxSwipeable(
                    state = expandableBoxState,
                    anchors = anchors,
                    enabled = expandableBoxState.currentValue != ExpandableBoxStateValue.HIDE,
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
                val progressState = expandableBoxState.getProgressState(foldHeightPx, maxHeightPx)
                val progress = if (progressState == ExpandableBoxStateValue.HIDE || progressState == ExpandableBoxStateValue.HIDING) {
                    if (foldHeightPx != 0f) {
                        (expandableBoxState.offset.value / (foldHeightPx)).coerceIn(0f, 1f)
                    } else {
                        1f
                    }
                } else {
                    if (maxHeightPx != foldHeightPx) {
                        ((expandableBoxState.offset.value - foldHeightPx) / (maxHeightPx - foldHeightPx)).coerceIn(0f, 1f)
                    } else {
                        1f
                    }
                }
                ExpandableBoxScope(
                    progress,
                    progressState,
                    expandableBoxState.currentValue,
                    this
                ).content()
            }
        }
    }
}

private fun ExpandableBoxState.getProgressState(foldHeightPx: Float, maxHeightPx: Float): ExpandableBoxStateValue {
    return when {
        offset.value <= 0 -> ExpandableBoxStateValue.HIDE
        offset.value < foldHeightPx -> ExpandableBoxStateValue.HIDING
        offset.value == foldHeightPx -> ExpandableBoxStateValue.FOLD
        offset.value > foldHeightPx && offset.value < maxHeightPx -> ExpandableBoxStateValue.FOLDING
        else -> ExpandableBoxStateValue.EXPAND
    }
}