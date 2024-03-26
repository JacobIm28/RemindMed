package com.gradle.controller

import com.gradle.constants.GlobalObjects
import com.gradle.models.Doctor
import com.gradle.ui.views.shared.ProfileViewEvent
import com.gradle.apiCalls.Doctor as DoctorApi

class DoctorController(val model: Doctor) {
    fun invoke(event: ProfileViewEvent, value: Any?) {
        when(event) {
            ProfileViewEvent.NameEvent -> model.name = value as String
            ProfileViewEvent.EmailEvent -> model.email = value as String
            ProfileViewEvent.UpdateEvent -> {
                val newDoc: Doctor = Doctor(GlobalObjects.doctor.did, model.name, model.email)
                try {
                    model.successfulChange = DoctorApi().updateDoctor(newDoc)
                } catch (e: Exception) {
                    model.errorMessage = e.message.toString()
                    model.isError = true
                }
            }
        }
    }
}