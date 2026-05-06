package io.groovin.expandablebox.sampleapp.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.groovin.expandablebox.sampleapp.ui.theme.CalendarAccent
import io.groovin.expandablebox.sampleapp.ui.theme.CalendarSurface

@Composable
internal fun CalendarHeader(
    onTodayClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${monthName(FocusedMonth)} $FocusedYear",
            color = CalendarAccent,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(CalendarSurface)
                .then(if (enabled) Modifier.clickable { onTodayClick() } else Modifier)
                .size(width = 64.dp, height = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Today",
                color = CalendarAccent,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
