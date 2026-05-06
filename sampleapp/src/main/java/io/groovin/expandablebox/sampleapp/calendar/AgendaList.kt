package io.groovin.expandablebox.sampleapp.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.groovin.expandablebox.sampleapp.ui.theme.CalendarAccent
import io.groovin.expandablebox.sampleapp.ui.theme.CalendarMuted
import io.groovin.expandablebox.sampleapp.ui.theme.CalendarSurface

@Composable
internal fun AgendaList(
    selected: CalendarDate,
    modifier: Modifier = Modifier
) {
    val events = eventsFor(selected)
    Column(modifier = modifier.fillMaxSize()) {
        AgendaHeader(selected = selected)
        Spacer(modifier = Modifier.height(8.dp))
        if (events.isEmpty()) {
            EmptyAgenda(modifier = Modifier.fillMaxWidth())
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 24.dp)
            ) {
                items(events) { event ->
                    AgendaEventCard(event = event)
                }
            }
        }
    }
}

@Composable
private fun AgendaHeader(selected: CalendarDate) {
    val isToday = isSameDay(selected, Today)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = longDateLabel(selected),
            color = CalendarAccent,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
        if (isToday) {
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(CalendarAccent.copy(alpha = 0.15f))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "Today",
                    color = CalendarAccent,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun AgendaEventCard(event: SampleEvent) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CalendarSurface)
            .padding(14.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .height(40.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(event.color)
        )
        Column(
            modifier = Modifier
                .padding(start = 12.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = event.timeRange,
                color = CalendarMuted,
                fontSize = 11.sp
            )
            Text(
                modifier = Modifier.padding(top = 2.dp),
                text = event.title,
                color = CalendarAccent,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
            event.location?.let { loc ->
                Text(
                    modifier = Modifier.padding(top = 2.dp),
                    text = loc,
                    color = CalendarMuted,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
private fun EmptyAgenda(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.padding(vertical = 36.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Nothing scheduled",
            color = CalendarMuted,
            fontSize = 14.sp
        )
    }
}
