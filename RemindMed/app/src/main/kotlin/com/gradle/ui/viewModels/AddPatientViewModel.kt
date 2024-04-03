package com.gradle.ui.viewModels

import androidx.compose.runtime.mutableStateOf
import com.gradle.models.AddPatient
import com.gradle.ui.viewModels.ISubscriber

class AddPatientViewModel(val model: AddPatient) : ISubscriber {
    val did = model.did
    var email = mutableStateOf(model.email)
    var emailIsValid = mutableStateOf(model.emailIsValid)
    var currPatients = mutableStateOf(model.currPatients)
    var showPotentialPatient = mutableStateOf(model.showPotentialPatient)
    var currPatient = mutableStateOf(model.currPatient)
    var patientAlreadyUnderDoctor = mutableStateOf(model.patientAlreadyUnderDoctor)
    var showDialog = mutableStateOf(model.showDialog)
    var addPatientDialogMessage = mutableStateOf(model.addPatientDialogMessage)
    var submitEnabled = mutableStateOf(model.submitEnabled)

    init {
        model.subscribe(this)
    }

    override fun update() {
        email.value = model.email
        emailIsValid.value = model.emailIsValid
        currPatients.value = model.currPatients
        showPotentialPatient.value = model.showPotentialPatient
        currPatient.value = model.currPatient
        patientAlreadyUnderDoctor.value = model.patientAlreadyUnderDoctor
        showDialog.value = model.showDialog
        addPatientDialogMessage.value = model.addPatientDialogMessage
        submitEnabled.value = model.submitEnabled
    }
}