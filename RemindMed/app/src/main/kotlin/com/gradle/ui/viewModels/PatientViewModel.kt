package com.gradle.ui.viewModels

import androidx.compose.runtime.mutableStateOf
import com.gradle.models.Patient

class PatientViewModel(val model: Patient) : ISubscriber {
    var name = mutableStateOf(model.name)
    var email = mutableStateOf(model.email)
    var successfulChange = mutableStateOf(model.successfulChange)
    var errorMessage = mutableStateOf(model.errorMessage)
    var isError = mutableStateOf(model.isError)
    var submitEnabled = mutableStateOf(model.submitEnabled)
    var changesSubmitted = mutableStateOf(model.changesSubmitted)

    init {
        model.subscribe(this)
    }

    override fun update() {
        name.value = model.name
        email.value = model.email
        successfulChange.value = model.successfulChange
        errorMessage.value = model.errorMessage
        isError.value = model.isError
        submitEnabled.value = model.submitEnabled
        changesSubmitted.value = model.changesSubmitted
    }
}