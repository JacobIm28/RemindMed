package com.cs346.remindmed.models

import com.gradle.models.Doctor
import org.junit.Assert.assertEquals
import org.junit.Test

internal class DoctorModelTest {
    @Test
    fun initializeTest() {
        val doctor = Doctor("1a", "Test doctor", "testdoctor@gmail.com")
        assertEquals(doctor.did, "1a")
        assertEquals(doctor.name, "Test doctor")
        assertEquals(doctor.email, "testdoctor@gmail.com")
    }

    @Test
    fun toStringTest() {
        val doctor = Doctor("1a", "Test doctor", "testdoctor@gmail.com")
        val doctorString = doctor.toString()
        assertEquals(
            doctorString,
            "Doctor(did=1a, name='Test doctor', email='testdoctor@gmail.com')"
        )
    }
}