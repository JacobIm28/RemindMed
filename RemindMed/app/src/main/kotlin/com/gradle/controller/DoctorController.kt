package com.gradle.controller

import com.gradle.constants.GlobalObjects
import com.gradle.models.Doctor
import com.gradle.ui.viewModels.LoginViewModel
import com.gradle.ui.views.shared.ProfileViewEvent
import com.gradle.apiCalls.DoctorApi as DoctorApi
import com.gradle.apiCalls.User as UserApi

class DoctorController(val model: Doctor, private val loginViewModel: LoginViewModel) {
    fun invoke(event: ProfileViewEvent, value: Any?) {
        when(event) {
            ProfileViewEvent.NameEvent -> {
                model.name = value as String
                model.submitEnabled = value != model._name && value.isNotEmpty()
            }
            ProfileViewEvent.EmailEvent -> {
                model.email = value as String
                val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\$")
                val emailIsValid : Boolean =  emailRegex.matches(value.toString())
                model.submitEnabled = emailIsValid && value != model._email && value.isNotEmpty()
            }
            ProfileViewEvent.UpdateEvent -> {
                model.changesSubmitted = true
                val newDoc: Doctor = Doctor(GlobalObjects.doctor.did, model.name, model.email)
                try {
                    model.successfulChange = DoctorApi().updateDoctor(newDoc) && UserApi().changeEmail(newDoc.did, model.email)
                    if (model.successfulChange) {
                        model._name = model.name
                        model._email = model.email
                        model.submitEnabled = false
                        GlobalObjects.doctor = newDoc
                    }
                } catch (e: Exception) {
                    model.errorMessage = e.message.toString()
                    model.isError = true
                }
            }
            ProfileViewEvent.DismissEvent -> {
                model.changesSubmitted = false
                model.logoutClicked = false
            }
            ProfileViewEvent.LogoutClicked -> {
                model.logoutClicked = true
            }
            ProfileViewEvent.LogoutConfirmed -> {
                model.logoutClicked = false
                loginViewModel.logout()
            }
        }
    }
}