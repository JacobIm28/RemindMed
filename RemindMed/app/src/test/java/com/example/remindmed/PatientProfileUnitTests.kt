package com.cs346.remindmed

import com.gradle.controller.PatientController
import com.gradle.models.Patient
import com.gradle.ui.viewModels.LoginViewModel
import com.gradle.ui.viewModels.PatientViewModel
import com.gradle.ui.views.shared.ProfileViewEvent
import org.junit.Assert.assertEquals
import org.junit.Test
import android.text.TextUtils
import android.util.Base64


internal class PatientProfileUnitTests {

    @Test
    fun viewModelTests() {
        val testPat: Patient = Patient("1", "Gen", "gen@gmail.com")
        val viewModPat: PatientViewModel = PatientViewModel(testPat)
        assertEquals(viewModPat.name.value, testPat.name)
        assertEquals(viewModPat.email.value, testPat.email)
        assertEquals(viewModPat.successfulChange.value, testPat.successfulChange)
        assertEquals(viewModPat.submitEnabled.value, testPat.submitEnabled)

        val testPat2: Patient = Patient("1", "Gen", "gen@gmail.com")
        val viewModPat2: PatientViewModel = PatientViewModel(testPat2)
        testPat2.name = "Ben"
        assertEquals("Ben", viewModPat2.name.value)

        val testPat3: Patient = Patient("1", "Gen", "gen@gmail.com")
        val viewModPat3: PatientViewModel = PatientViewModel(testPat3)
        testPat3.email = "ben@gmail.com"
        assertEquals("ben@gmail.com", viewModPat3.email.value)
    }

    @Test
    fun controllerTests() {
        val loginViewModel = LoginViewModel()

        val testPat1: Patient = Patient("1", "Gen", "gen@gmail.com")
        val controller = PatientController(testPat1, loginViewModel)
        controller.invoke(ProfileViewEvent.NameEvent, "Jacob")
        assertEquals("Jacob", testPat1.name)

        val testPat2: Patient = Patient("1", "Gen", "gen@gmail.com")
        val controller2 = PatientController(testPat2, loginViewModel)
        controller2.invoke(ProfileViewEvent.EmailEvent, "not an email")
        assertEquals(false, testPat2.submitEnabled)

        val testPat3: Patient = Patient("1", "Gen", "gen@gmail.com")
        val controller3 = PatientController(testPat3, loginViewModel)
        controller3.invoke(ProfileViewEvent.DismissEvent, "")
        assertEquals(false, testPat3.changesSubmitted)
    }

    @Test
    fun allAspects() {
        val loginViewModel = LoginViewModel()

        val testPat1: Patient = Patient("1", "Gen", "gen@gmail.com")
        val controller1 = PatientController(testPat1, loginViewModel)
        val viewModPat1: PatientViewModel = PatientViewModel(testPat1)
        viewModPat1.email.value = "gen2@gmail.com"
        controller1.invoke(ProfileViewEvent.UpdateEvent, "")
        assertEquals(true, testPat1.changesSubmitted)

        val testPat2: Patient = Patient("1", "Gen", "gen@gmail.com")
        val controller2 = PatientController(testPat2, loginViewModel)
        val viewModPat2: PatientViewModel = PatientViewModel(testPat2)
        controller2.invoke(ProfileViewEvent.LogoutClicked, "")
        assertEquals(true, viewModPat2.logoutClicked.value)
    }
}