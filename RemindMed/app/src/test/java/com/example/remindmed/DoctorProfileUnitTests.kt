package com.cs346.remindmed

import com.gradle.controller.DoctorController
import com.gradle.models.Doctor
import com.gradle.ui.viewModels.DoctorViewModel
import com.gradle.ui.viewModels.LoginViewModel
import com.gradle.ui.views.shared.ProfileViewEvent
import org.junit.Assert.assertEquals
import org.junit.Test
import android.text.TextUtils
import android.util.Base64


internal class DoctorProfileUnitTests {

    @Test
    fun viewModelTests() {
        val testDoc: Doctor = Doctor("1", "Gen", "gen@gmail.com")
        val viewModDoc: DoctorViewModel = DoctorViewModel(testDoc)
        assertEquals(viewModDoc.name.value, testDoc.name)
        assertEquals(viewModDoc.email.value, testDoc.email)
        assertEquals(viewModDoc.successfulChange.value, testDoc.successfulChange)
        assertEquals(viewModDoc.submitEnabled.value, testDoc.submitEnabled)

        val testDoc2: Doctor = Doctor("1", "Gen", "gen@gmail.com")
        val viewModDoc2: DoctorViewModel = DoctorViewModel(testDoc2)
        testDoc2.name = "Ben"
        assertEquals("Ben", viewModDoc2.name.value)

        val testDoc3: Doctor = Doctor("1", "Gen", "gen@gmail.com")
        val viewModDoc3: DoctorViewModel = DoctorViewModel(testDoc3)
        testDoc3.email = "ben@gmail.com"
        assertEquals("ben@gmail.com", viewModDoc3.email.value)
    }

    @Test
    fun controllerTests() {
        val loginViewModel = LoginViewModel()

        val testDoc1: Doctor = Doctor("1", "Gen", "gen@gmail.com")
        val controller = DoctorController(testDoc1, loginViewModel)
        controller.invoke(ProfileViewEvent.NameEvent, "Jacob")
        assertEquals("Jacob", testDoc1.name)

        val testDoc2: Doctor = Doctor("1", "Gen", "gen@gmail.com")
        val controller2 = DoctorController(testDoc2, loginViewModel)
        controller2.invoke(ProfileViewEvent.EmailEvent, "not an email")
        assertEquals(false, testDoc2.submitEnabled)

        val testDoc3: Doctor = Doctor("1", "Gen", "gen@gmail.com")
        val controller3 = DoctorController(testDoc3, loginViewModel)
        controller3.invoke(ProfileViewEvent.DismissEvent, "")
        assertEquals(false, testDoc3.changesSubmitted)
    }

    @Test
    fun allAspects() {
        val loginViewModel = LoginViewModel()

        val testDoc1: Doctor = Doctor("1", "Gen", "gen@gmail.com")
        val controller1 = DoctorController(testDoc1, loginViewModel)
        val viewModDoc1: DoctorViewModel = DoctorViewModel(testDoc1)
        viewModDoc1.email.value = "gen2@gmail.com"
        controller1.invoke(ProfileViewEvent.UpdateEvent, "")
        assertEquals(true, testDoc1.changesSubmitted)

        val testDoc2: Doctor = Doctor("1", "Gen", "gen@gmail.com")
        val controller2 = DoctorController(testDoc2, loginViewModel)
        val viewModDoc2: DoctorViewModel = DoctorViewModel(testDoc2)
        controller2.invoke(ProfileViewEvent.LogoutClicked, "")
        assertEquals(true, viewModDoc2.logoutClicked.value)
    }
}