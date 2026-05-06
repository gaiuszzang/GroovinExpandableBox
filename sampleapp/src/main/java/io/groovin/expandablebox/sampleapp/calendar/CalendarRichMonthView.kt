package io.groovin.expandablebox.sampleapp.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.groovin.expandablebox.sampleapp.ui.theme.CalendarAccent
import io.groovin.expandablebox.sampleapp.ui.theme.CalendarMuted

private const val MaxEventLines = 4

@Composable
internal fun CalendarRichMonthView(
    selected: CalendarDate,
    onDateClick: (CalendarDate) -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        WeekdayLabelRow()
        Spacer(modifier = Modifier.height(6.dp))
        MonthGrid.chunked(7).forEach { week ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalAlignment = Alignment.Top
            ) {
                week.forEach { date ->
                    RichDayCell(
                        date = date,
                        selected = selected,
                        events = eventsFor(date),
                        enabled = enabled,
                        onClick = { onDateClick(date) }
                    )
                }
            }
        }
    }
}

@Composable
private fun RowScope.RichDayCell(
    date: CalendarDate,
    selected: CalendarDate,
    events: List<SampleEvent>,
    enabled: Boolean,
    onClick: () -> Unit
) {
    val inFocusedMonth = isInFocusedMonth(date)
    val isToday = isSameDay(date, Today)
    val isSelected = isSameDay(date, selected) && inFocusedMonth
    val isClickable = enabled && inFocusedMonth
    val numberBg = when {
        isSelected -> CalendarAccent
        isToday -> CalendarAccent.copy(alpha = 0.18f)
        else -> Color.Transparent
    }
    val numberFg = when {
        isSelected -> Color.White
        !inFocusedMonth -> CalendarMuted.copy(alpha = 0.5f)
        isToday -> CalendarAccent
        else -> Color(0xFF2C2A30)
    }
    Column(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .padding(horizontal = 1.dp, vertical = 2.dp)
            .then(if (isClickable) Modifier.clickable { onClick() } else Modifier),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Box(
            modifier = Modifier
                .size(22.dp)
                .clip(CircleShape)
                .background(numberBg),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = date.day.toString(),
                color = numberFg,
                fontSize = 11.sp,
                fontWeight = if (isToday || isSelected) FontWeight.SemiBold else FontWeight.Normal
            )
        }
        if (inFocusedMonth) {
            Spacer(modifier = Modifier.height(3.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                events.take(MaxEventLines).forEach { event ->
                    EventChip(event = event)
                }
            }
        }
    }
}

@Composable
private fun EventChip(event: SampleEvent) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(3.dp))
            .background(event.color)
            .padding(horizontal = 4.dp, vertical = 1.dp)
    ) {
        Text(
            text = event.title,
            color = Color.White,
            fontSize = 9.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
