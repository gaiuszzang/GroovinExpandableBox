package io.groovin.expandablebox.sampleapp.calendar

import androidx.compose.ui.graphics.Color

internal data class CalendarDate(
    val year: Int,
    val month: Int, // 0-indexed
    val day: Int
)

internal const val FocusedYear = 2026
internal const val FocusedMonth = 3 // April (0-indexed)
internal const val DaysInFocusedMonth = 30

// April 1, 2026 = Wednesday. 0=Sun, 1=Mon, ... 6=Sat
private const val APRIL_FIRST_DOW = 3

internal val Today = CalendarDate(FocusedYear, FocusedMonth, 14) // Tuesday

private val MonthNames = listOf(
    "January", "February", "March", "April", "May", "June",
    "July", "August", "September", "October", "November", "December"
)

private val DayNamesLong = listOf(
    "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
)

internal val WeekdayHeaders: List<String> = listOf("S", "M", "T", "W", "T", "F", "S")

internal data class SampleEvent(
    val title: String,
    val timeRange: String,
    val location: String?,
    val color: Color
)

private fun event(title: String, timeRange: String, location: String?, color: Color) =
    SampleEvent(title, timeRange, location, color)

internal val EventsByDate: Map<CalendarDate, List<SampleEvent>> = mapOf(
    CalendarDate(2026, 3, 13) to listOf(
        event("Yoga", "07:30 — 08:30", "Studio Bloom", Color(0xFF34A853))
    ),
    CalendarDate(2026, 3, 14) to listOf(
        event("Team standup", "10:00 — 10:30", "Meet · Engineering", Color(0xFF4285F4)),
        event("Design review", "13:00 — 14:00", "Room Aurora", Color(0xFFEFB8C8)),
        event("1:1 with Mark", "16:30 — 17:00", null, Color(0xFF34A853))
    ),
    CalendarDate(2026, 3, 15) to listOf(
        event("Sprint planning", "11:00 — 12:00", "Meet · Squad", Color(0xFF4285F4)),
        event("Lunch with Sara", "12:30 — 13:30", "Cafe Lumen", Color(0xFFFBBC05))
    ),
    CalendarDate(2026, 3, 16) to listOf(
        event("Dentist", "09:00 — 09:45", "Maple Dental", Color(0xFFEA4335))
    ),
    CalendarDate(2026, 3, 17) to listOf(
        event("Friday demo", "15:00 — 16:00", "All-hands room", Color(0xFF7D5260)),
        event("Beer with team", "19:00 — 21:00", "Local pub", Color(0xFFFBBC05))
    ),
    CalendarDate(2026, 3, 21) to listOf(
        event("Quarterly review", "14:00 — 15:30", "Meet · Leads", Color(0xFF4285F4))
    )
)

internal fun eventsFor(date: CalendarDate): List<SampleEvent> = EventsByDate[date].orEmpty()
internal fun eventCountFor(date: CalendarDate): Int = eventsFor(date).size

internal fun isSameDay(a: CalendarDate, b: CalendarDate): Boolean =
    a.year == b.year && a.month == b.month && a.day == b.day

internal fun isInFocusedMonth(date: CalendarDate): Boolean =
    date.year == FocusedYear && date.month == FocusedMonth

// Index 0 = April 1, 2026. Negative = March, ≥30 = May
private fun toDayIndex(date: CalendarDate): Int = when {
    date.year == FocusedYear && date.month == 2 -> date.day - 32
    date.year == FocusedYear && date.month == 3 -> date.day - 1
    date.year == FocusedYear && date.month == 4 -> date.day - 1 + DaysInFocusedMonth
    else -> 0
}

private fun dateFromIndex(index: Int): CalendarDate = when {
    index < 0 -> CalendarDate(FocusedYear, 2, 32 + index)
    index < DaysInFocusedMonth -> CalendarDate(FocusedYear, 3, index + 1)
    else -> CalendarDate(FocusedYear, 4, index - DaysInFocusedMonth + 1)
}

internal fun dayOfWeek(date: CalendarDate): Int {
    val index = toDayIndex(date)
    return ((APRIL_FIRST_DOW + index) % 7 + 7) % 7
}

internal fun weekDates(reference: CalendarDate): List<CalendarDate> {
    val dow = dayOfWeek(reference)
    val sundayIndex = toDayIndex(reference) - dow
    return List(7) { i -> dateFromIndex(sundayIndex + i) }
}

internal val MonthGrid: List<CalendarDate> = run {
    // Sunday before April 1: Apr 1 is Wed, so Sun is 3 days before = Mar 29 (index -3)
    val startIndex = -APRIL_FIRST_DOW
    List(42) { i -> dateFromIndex(startIndex + i) }
}

internal fun monthName(month: Int): String = MonthNames.getOrNull(month) ?: ""

internal fun longDateLabel(date: CalendarDate): String {
    val dow = dayOfWeek(date)
    return "${DayNamesLong[dow]}, ${monthName(date.month)} ${date.day}"
}
