package com.gradle.controller

import com.gradle.constants.GlobalObjects
import com.gradle.models.Doctor
import com.gradle.ui.views.shared.ProfileViewEvent
import com.gradle.apiCalls.Doctor as DoctorApi

class DoctorController(val model: Doctor) {
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
                val newDoc: Doctor = Doctor(GlobalObjects.doctor.did, model.name, model.email)
                try {
                    model.successfulChange = DoctorApi().updateDoctor(newDoc)
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