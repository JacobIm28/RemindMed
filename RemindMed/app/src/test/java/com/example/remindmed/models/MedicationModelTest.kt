package com.example.remindmed.models

import com.gradle.models.Medication
import org.junit.Assert.assertEquals
import org.junit.Test
import java.sql.Date
import java.sql.Time

internal class MedicationModelTest {
    private val testMedication: Medication = Medication(
        "1a",
        "abc123",
        "20mg",
        Date(2024, 4, 10),
        Date(2024, 4, 15),
        "Advil",
        "Take 3 times a day",
        mutableListOf(Time(10, 30, 0)),
        false,
        false
    )

    @Test
    fun initializeTest() {
        assertEquals(testMedication.pid, "1a")
        assertEquals(testMedication.medicationId, "abc123")
        assertEquals(testMedication.name, "Advil")
        assertEquals(testMedication.amount, "20mg")
        assertEquals(testMedication.startDate, Date(2024, 4, 10))
        assertEquals(testMedication.endDate, Date(2024, 4, 15))
        assertEquals(testMedication.notes, "Take 3 times a day")
        assertEquals(testMedication.times, mutableListOf<Time>(Time(10, 30, 0)))
        assertEquals(testMedication.accepted, false)
        assertEquals(testMedication.taken, false)
    }

    @Test
    fun toStringTest() {
        val medicationString = testMedication.toString()
        assertEquals(
            medicationString,
            "Medication(pid=1a, medicationId=abc123, amount='20mg', startDate=3924-05-10, endDate=3924-05-15, name=Advil, notes=Take 3 times a day, times=[10:30:00], accepted=false, taken=false)"
        )
    }

    @Test
    fun getFormattedTimesTest() {
        val formattedTimes = testMedication.getFormattedTimes()
        assertEquals(formattedTimes.size, 1)
        assertEquals(formattedTimes[0], "10:30 AM")
    }

    @Test
    fun equalsTest() {
        val testMedication2 = Medication(
            "1a",
            "abc123",
            "20mg",
            Date(2024, 4, 10),
            Date(2024, 4, 15),
            "Advil",
            "Take 3 times a day",
            mutableListOf<Time>(Time(10, 30, 0)),
            false,
            false
        )
        var result = testMedication.equals(testMedication2)
        assertEquals(result, true)

        val testMedication3 = Medication(
            "1b",
            "Test medication",
            "20mg",
            Date(2024, 4, 10),
            Date(2024, 4, 15),
            "Take with food",
            "Take 3 times a day",
            mutableListOf<Time>(Time(10, 30, 0)),
            false,
            false
        )
        result = testMedication.equals(testMedication3)
        assertEquals(result, false)

        val testMedication4 = Medication(
            "1a",
            "Test medication",
            "20mg",
            Date(2024, 4, 10),
            Date(2024, 4, 15),
            "Take with food",
            "Take 3 times a day",
            mutableListOf<Time>(),
            false,
            false
        )
        result = testMedication.equals(testMedication4)
        assertEquals(result, false)
    }

    @Test
    fun hashCodeTest() {
        val result = testMedication.hashCode()
        assertEquals(result, -2071225473)
    }
}