package com.gradle.controller

import com.gradle.constants.GlobalObjects
import com.gradle.models.Patient
import com.gradle.ui.views.shared.ProfileViewEvent

class PatientController(val model: Patient) {
    fun invoke(event: ProfileViewEvent, value: Any?) {
        when(event) {
            ProfileViewEvent.NameEvent -> {
                model.name = value as String
                model.submitEnabled = value != model._name
            }
            ProfileViewEvent.EmailEvent -> {
                model.email = value as String
                val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\$")
                val emailIsValid : Boolean =  emailRegex.matches(value.toString())
                model.submitEnabled = emailIsValid && value != model._email
            }
            ProfileViewEvent.UpdateEvent -> {
                model.changesSubmitted = true
                val newPat: Patient = Patient(GlobalObjects.patient.pid, model.name, model.email)
                try {
                    model.successfulChange = com.gradle.apiCalls.Patient().updatePatient(newPat)
                    if (model.successfulChange) {
                        model._name = model.name
                        model._email = model.email
                        model.submitEnabled = false
                    }
                } catch (e: Exception) {
                    model.errorMessage = e.message.toString()
                    model.isError = true
                }
            }
            ProfileViewEvent.DismissEvent -> {
                model.changesSubmitted = false
            }
        }
    }
}