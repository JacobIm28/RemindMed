package com.example.remindmed

import com.gradle.controller.DoctorController
import com.gradle.controller.PatientController
import com.gradle.models.Doctor
import com.gradle.models.Patient
import com.gradle.ui.views.DoctorViewModel
import com.gradle.ui.views.PatientViewModel
import com.gradle.ui.views.shared.ProfileViewEvent
import org.junit.Test
import org.junit.Assert.*


internal class PatientProfileUnitTests {
    private val testPat : Patient = Patient("1", "Gen", "gen@gmail.com")

    @Test
    fun viewModelTests() {
        // changing the various fields
        var viewModDoc : PatientViewModel = PatientViewModel(testPat)

        assertEquals(viewModDoc.name.value, testPat.name)
        assertEquals(viewModDoc.email.value, testPat.email)
        assertEquals(viewModDoc.successfulChange.value, testPat.successfulChange)
        assertEquals(viewModDoc.submitEnabled.value, testPat.submitEnabled)

        testPat.name = "Ben"
        assertEquals("Ben", viewModDoc.name.value)
        testPat.email = "ben@gmail.com"
        assertEquals("ben@gmail.com", viewModDoc.email.value)
    }

    @Test
    fun controllerTests() {
        val controller = PatientController(testPat)

        controller.invoke(ProfileViewEvent.NameEvent, "Jacob")
        assertEquals("Jacob", testPat.name)

        controller.invoke(ProfileViewEvent.EmailEvent, "not an email")
        assertEquals(false, testPat.submitEnabled)

        controller.invoke(ProfileViewEvent.DismissEvent, "")
        assertEquals(false, testPat.changesSubmitted)
    }
}