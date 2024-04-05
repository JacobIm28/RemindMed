package com.gradle.models

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class CalendarModel(
    val selectedDate: DateModel,
    val visibleDates: List<DateModel>
) {
    val startDate: DateModel = visibleDates.first()
    val endDate: DateModel = visibleDates.last()

    val monthNames = mapOf(
        1 to "Jan",
        2 to "Feb",
        3 to "Mar",
        4 to "Apr",
        5 to "May",
        6 to "Jun",
        7 to "Jul",
        8 to "Aug",
        9 to "Sep",
        10 to "Oct",
        11 to "Nov",
        12 to "Dec"
    )
}
data class DateModel(
    val date: Date,
    val isSelected: Boolean,
    val isToday: Boolean
) {
    val day: String = SimpleDateFormat("E", Locale.getDefault()).format(date) ?: ""
}