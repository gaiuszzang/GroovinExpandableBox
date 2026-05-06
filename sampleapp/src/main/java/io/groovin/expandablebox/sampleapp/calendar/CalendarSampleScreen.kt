package io.groovin.expandablebox.sampleapp.calendar

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import io.groovin.expandablebox.ExpandableBox
import io.groovin.expandablebox.ExpandableBoxStateValue
import io.groovin.expandablebox.ExpandableBoxSwipeDirection
import io.groovin.expandablebox.rememberExpandableBoxState
import io.groovin.expandablebox.sampleapp.ui.theme.CalendarPanelBackground
import io.groovin.expandablebox.sampleapp.ui.theme.CalendarSurface
import kotlinx.coroutines.launch

private val FoldHeight = 200.dp
private val HalfExpandHeight = 480.dp
private val CardCornerRadius = 20.dp
private val CardElevation = 12.dp
private val CardHorizontalMargin = 16.dp
private val CardTopMargin = 12.dp
private val CardBottomMargin = 16.dp

@Composable
fun CalendarSampleScreen() {
    val coroutineScope = rememberCoroutineScope()
    var selected by remember { mutableStateOf(Today) }
    val statusBarPadding = WindowInsets.statusBars.asPaddingValues()
    val expandableBoxState = rememberExpandableBoxState(
        initialValue = ExpandableBoxStateValue.HalfExpand
    )

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .safeContentPadding()
            .background(CalendarPanelBackground)
    ) {
        val containerMaxHeight = maxHeight
        val expandHeight = (containerMaxHeight
            - statusBarPadding.calculateTopPadding()
            - CardTopMargin
            - CardBottomMargin)
            .coerceAtLeast(HalfExpandHeight + 1.dp)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = statusBarPadding.calculateTopPadding() + CardTopMargin,
                    bottom = CardBottomMargin
                )
        ) {
            ExpandableBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = CardHorizontalMargin)
                    .shadow(elevation = CardElevation, shape = RoundedCornerShape(CardCornerRadius))
                    .background(CalendarSurface, RoundedCornerShape(CardCornerRadius))
                    .testTag("CalendarExpandableBox"),
                expandableBoxState = expandableBoxState,
                swipeDirection = ExpandableBoxSwipeDirection.SwipeDownToExpand,
                foldHeight = FoldHeight,
                halfExpandHeight = HalfExpandHeight,
                expandHeight = expandHeight
            ) {
                val alphas = computeAlphas(progressState, progress)
                val isFoldDominant = progressState == ExpandableBoxStateValue.Fold
                val isHalfDominant = progressState == ExpandableBoxStateValue.HalfExpand
                val isExpandDominant = progressState == ExpandableBoxStateValue.Expand
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    RichMonthPanel(
                        modifier = Modifier
                            .fillMaxWidth()
                            .alpha(alphas.expand),
                        selected = selected,
                        enabled = isExpandDominant,
                        onTodayClick = { selected = Today },
                        onDateClick = { selected = it }
                    )
                    MonthPanel(
                        modifier = Modifier
                            .fillMaxWidth()
                            .alpha(alphas.half),
                        selected = selected,
                        enabled = isHalfDominant,
                        onTodayClick = { selected = Today },
                        onDateClick = { selected = it }
                    )
                    WeekPanel(
                        modifier = Modifier
                            .fillMaxWidth()
                            .alpha(alphas.fold),
                        selected = selected,
                        enabled = isFoldDominant,
                        onTodayClick = { selected = Today },
                        onDateClick = { selected = it }
                    )
                }
                BackHandler(
                    enabled = (completedState != ExpandableBoxStateValue.Fold) || expandableBoxState.isAnimationRunning
                ) {
                    if (expandableBoxState.isAnimationRunning) return@BackHandler
                    coroutineScope.launch {
                        expandableBoxState.animateTo(ExpandableBoxStateValue.Fold)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            AgendaList(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                selected = selected
            )
        }
    }
}

private data class ViewAlphas(val fold: Float, val half: Float, val expand: Float)

private fun computeAlphas(
    progressState: ExpandableBoxStateValue,
    progress: Float
): ViewAlphas = when (progressState) {
    ExpandableBoxStateValue.Fold -> ViewAlphas(1f, 0f, 0f)
    ExpandableBoxStateValue.Folding -> ViewAlphas(1f - progress, progress, 0f)
    ExpandableBoxStateValue.HalfExpand -> ViewAlphas(0f, 1f, 0f)
    ExpandableBoxStateValue.Expanding -> ViewAlphas(0f, 1f - progress, progress)
    ExpandableBoxStateValue.Expand -> ViewAlphas(0f, 0f, 1f)
}

@Composable
private fun WeekPanel(
    selected: CalendarDate,
    enabled: Boolean,
    onTodayClick: () -> Unit,
    onDateClick: (CalendarDate) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        CalendarHeader(onTodayClick = onTodayClick, enabled = enabled)
        Box(modifier = Modifier.padding(top = 14.dp)) {
            CalendarWeekView(selected = selected, onDateClick = onDateClick, enabled = enabled)
        }
    }
}

@Composable
private fun MonthPanel(
    selected: CalendarDate,
    enabled: Boolean,
    onTodayClick: () -> Unit,
    onDateClick: (CalendarDate) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        CalendarHeader(onTodayClick = onTodayClick, enabled = enabled)
        Box(modifier = Modifier.padding(top = 14.dp)) {
            CalendarMonthView(selected = selected, onDateClick = onDateClick, enabled = enabled)
        }
    }
}

@Composable
private fun RichMonthPanel(
    selected: CalendarDate,
    enabled: Boolean,
    onTodayClick: () -> Unit,
    onDateClick: (CalendarDate) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        CalendarHeader(onTodayClick = onTodayClick, enabled = enabled)
        Box(
            modifier = Modifier
                .padding(top = 14.dp)
                .weight(1f)
                .fillMaxWidth()
        ) {
            CalendarRichMonthView(
                modifier = Modifier.fillMaxSize(),
                selected = selected,
                onDateClick = onDateClick,
                enabled = enabled
            )
        }
    }
}
