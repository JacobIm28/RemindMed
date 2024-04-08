package com.cs346.remindmed

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

        val viewModel2 = AddPatientViewModel(mod)
        mod.email = "test@gmail.com"
        assertEquals(mod.email, viewModel2.email.value)

        val viewModel3 = AddPatientViewModel(mod)
        mod.showDialog = true
        assertEquals(mod.showDialog, viewModel3.showDialog.value)
    }

    @Test
    fun controllerTests() {
        val controller = AddPatientController(mod)
        controller.invoke(AddPatientViewEvent.EmailEvent, "notavalidemail")
        assertEquals(false, mod.emailIsValid)

        val controller2 = AddPatientController(mod)
        controller2.invoke(AddPatientViewEvent.EmailEvent, "gen@gmail.com")
        assertEquals("gen@gmail.com", mod.email)

        val controller3 = AddPatientController(mod)
        mod.email = "nonexistant@gmail.com"
        controller3.invoke(AddPatientViewEvent.SearchPatientEvent, "")
        assertEquals(null, mod.currPatient)

        val controller4 = AddPatientController(mod)
        mod.currPatient = null
        controller4.invoke(AddPatientViewEvent.AddPatientEvent, "")
        assertEquals(true, mod.showDialog)

        val controller5 = AddPatientController(mod)
        controller5.invoke(AddPatientViewEvent.DialogClose, "")
        assertEquals(false, mod.showDialog)

        val mod2: AddPatient = AddPatient("1", mutableListOf<Patient>())
        val controller6 = AddPatientController(mod2)
        controller6.invoke(AddPatientViewEvent.AddPatientEvent, "")
        assertEquals(false, mod2.submitEnabled)
    }
}