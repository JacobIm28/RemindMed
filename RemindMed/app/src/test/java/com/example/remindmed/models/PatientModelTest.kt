package com.cs346.remindmed.models

import com.gradle.models.Patient
import org.junit.Assert.assertEquals
import org.junit.Test

internal class PatientModelTest {
    @Test
    fun initializeTest() {
        val patient = Patient("1a", "Test name", "testname@gmail.com")
        assertEquals(patient.pid, "1a")
        assertEquals(patient.name, "Test name")
        assertEquals(patient.email, "testname@gmail.com")
    }

    @Test
    fun toStringTest() {
        val patient = Patient("1a", "Test name", "testname@gmail.com")
        val patientString = patient.toString()
        assertEquals(patientString, "Patient(pid=1a, name='Test name', email='testname@gmail.com')")
    }
}