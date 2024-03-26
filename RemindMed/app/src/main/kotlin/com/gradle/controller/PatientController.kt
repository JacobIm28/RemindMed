package com.gradle.controller

import com.gradle.constants.GlobalObjects
import com.gradle.models.Patient
import com.gradle.ui.views.shared.ProfileViewEvent

class PatientController(val model: Patient) {
    fun invoke(event: ProfileViewEvent, value: Any?) {
        when(event) {
            ProfileViewEvent.NameEvent -> model.name = value as String
            ProfileViewEvent.EmailEvent -> model.email = value as String
            ProfileViewEvent.UpdateEvent -> {
                val newPat: Patient = Patient(GlobalObjects.patient.pid, model.name, model.email)
                try {
                    model.successfulChange = com.gradle.apiCalls.Patient().updatePatient(newPat)
                } catch (e: Exception) {
                    model.errorMessage = e.message.toString()
                    model.isError = true
                }
            }
        }
    }
}