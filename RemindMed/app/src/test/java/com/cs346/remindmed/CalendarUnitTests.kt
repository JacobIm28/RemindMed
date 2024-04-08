package com.gradle.utilities

import com.gradle.models.CalendarModel
import com.gradle.models.DateModel
import org.junit.Assert.assertEquals
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

class CalendarUnitTests {
    @Test
    fun testGetData() {
        val calendarDataSource = CalendarDataSource()
        val startDate = Date()
        val lastSelectedDate = startDate

        val calendar = Calendar.getInstance()
        calendar.time = startDate
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        val expectedFirstDayOfWeek = calendar.time

        calendar.add(Calendar.DAY_OF_YEAR, 7)
        val expectedEndDayOfWeek = calendar.time

        val result = calendarDataSource.getData(startDate, lastSelectedDate)

        assertEquals(expectedFirstDayOfWeek, result.startDate.date)
        assertEquals(expectedEndDayOfWeek, result.endDate.date)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val expectedDateList = mutableListOf<DateModel>()
        calendar.time = expectedFirstDayOfWeek
        while (calendar.time <= expectedEndDayOfWeek) {
            val date = calendar.time
            expectedDateList.add(
                DateModel(
                    date = date,
                    isSelected = date == lastSelectedDate,
                    isToday = calendarDataSource.isToday(date)
                )
            )
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        assertEquals(expectedDateList.size, result.visibleDates.size)
        for (i in expectedDateList.indices) {
            assertEquals(expectedDateList[i].date, result.visibleDates[i].date)
            assertEquals(expectedDateList[i].isSelected, result.visibleDates[i].isSelected)
            assertEquals(expectedDateList[i].isToday, result.visibleDates[i].isToday)
            assertEquals(expectedDateList[i].day, result.visibleDates[i].day)
        }
    }

    @Test
    fun testIsToday() {
        val calendarDataSource = CalendarDataSource()
        val todayDate = Date()
        val yesterdayDate = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }.time
        val tomorrowDate = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 1) }.time

        assertEquals(true, calendarDataSource.isToday(todayDate))
        assertEquals(false, calendarDataSource.isToday(yesterdayDate))
        assertEquals(false, calendarDataSource.isToday(tomorrowDate))
    }
}
