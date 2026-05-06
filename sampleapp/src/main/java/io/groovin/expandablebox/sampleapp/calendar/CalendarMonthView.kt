package io.groovin.expandablebox.sampleapp.calendar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun CalendarMonthView(
    selected: CalendarDate,
    onDateClick: (CalendarDate) -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        WeekdayLabelRow()
        Spacer(modifier = Modifier.height(6.dp))
        MonthGrid.chunked(7).forEach { week ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                week.forEach { date ->
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
}
