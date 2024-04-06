package com.example.remindmed

import com.gradle.controller.AddPatientController
import com.gradle.models.AddPatient
import com.gradle.models.Patient
import com.gradle.ui.viewModels.AddPatientViewModel
import com.gradle.ui.views.doctor.AddPatientViewEvent
import org.junit.Assert.assertEquals
import org.junit.Test

internal class AddPatientUnitTests {
    private val mod: AddPatient = AddPatient("1", mutableListOf<Patient>())

    @Test
    fun viewModelTests() {
        val viewModel = AddPatientViewModel(mod)

        assertEquals(mod.currPatients, viewModel.currPatients.value)
        assertEquals(mod.did, viewModel.did)

        mod.email = "test@gmail.com"
        assertEquals(mod.email, viewModel.email.value)

        mod.showDialog = true
        assertEquals(mod.showDialog, viewModel.showDialog.value)
    }

    @Test
    fun controllerTests() {
        val controller = AddPatientController(mod)

        controller.invoke(AddPatientViewEvent.EmailEvent, "notavalidemail")
        assertEquals(false, mod.emailIsValid)
        controller.invoke(AddPatientViewEvent.EmailEvent, "gen@gmail.com")
        assertEquals("gen@gmail.com", mod.email)

        mod.email = "nonexistant@gmail.com"
        controller.invoke(AddPatientViewEvent.SearchPatientEvent, "")
        assertEquals(null, mod.currPatient)

        mod.currPatient = null
        controller.invoke(AddPatientViewEvent.AddPatientEvent, "")
        assertEquals(true, mod.showDialog)

        controller.invoke(AddPatientViewEvent.DialogClose, "")
        assertEquals(false, mod.showDialog)
    }
}