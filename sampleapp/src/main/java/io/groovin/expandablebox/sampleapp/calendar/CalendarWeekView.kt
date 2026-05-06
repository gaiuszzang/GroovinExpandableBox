package io.groovin.expandablebox.sampleapp.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.groovin.expandablebox.sampleapp.ui.theme.CalendarAccent
import io.groovin.expandablebox.sampleapp.ui.theme.CalendarMuted

@Composable
internal fun CalendarWeekView(
    selected: CalendarDate,
    onDateClick: (CalendarDate) -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    val dates = weekDates(selected)
    Column(modifier = modifier.fillMaxWidth()) {
        WeekdayLabelRow()
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            dates.forEach { date ->
                DayCell(
                    date = date,
                    selected = selected,
                    eventCount = eventCountFor(date),
                    enabled = enabled,
                    onClick = { onDateClick(date) }
                )
            }
        }
    }
}

@Composable
internal fun WeekdayLabelRow(modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth()) {
        WeekdayHeaders.forEach { label ->
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    color = CalendarMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
internal fun RowScope.DayCell(
    date: CalendarDate,
    selected: CalendarDate,
    eventCount: Int,
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    val inFocusedMonth = isInFocusedMonth(date)
    val isToday = isSameDay(date, Today)
    val isSelected = isSameDay(date, selected) && inFocusedMonth
    val circleColor = when {
        isSelected -> CalendarAccent
        isToday -> CalendarAccent.copy(alpha = 0.18f)
        else -> Color.Transparent
    }
    val textColor = when {
        isSelected -> Color.White
        !inFocusedMonth -> CalendarMuted.copy(alpha = 0.5f)
        isToday -> CalendarAccent
        else -> Color(0xFF2C2A30)
    }
    val dotColor = if (isSelected) Color.White else CalendarAccent
    val isClickable = enabled && inFocusedMonth
    Column(
        modifier = modifier
            .weight(1f)
            .then(if (isClickable) Modifier.clickable { onClick() } else Modifier)
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(circleColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = date.day.toString(),
                color = textColor,
                fontSize = 14.sp,
                fontWeight = if (isToday || isSelected) FontWeight.SemiBold else FontWeight.Normal
            )
        }
        Box(
            modifier = Modifier
                .padding(top = 3.dp)
                .size(4.dp)
                .clip(CircleShape)
                .background(if (eventCount > 0 && inFocusedMonth) dotColor else Color.Transparent)
        )
    }
}
