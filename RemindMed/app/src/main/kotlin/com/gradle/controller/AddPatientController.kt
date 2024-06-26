package com.gradle.controller

import com.gradle.models.AddPatient
import com.gradle.models.Patient
import com.gradle.ui.views.doctor.AddPatientViewEvent
import com.gradle.apiCalls.PatientApi as PatientApi
import com.gradle.apiCalls.DoctorApi as DoctorApi

class AddPatientController(val model: AddPatient) {
    fun invoke(event: AddPatientViewEvent, value: Any?) {
        when (event) {
            AddPatientViewEvent.EmailEvent -> {
                val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\$")
                if (value == ""){
                    model.emailIsValid = true
                } else {
                    model.emailIsValid =  emailRegex.matches(value.toString())
                }
                model.email = value as String
            }
            AddPatientViewEvent.SearchPatientEvent -> {
                val potentialPatient: Patient = PatientApi().getPatientbyEmail(model.email)
                var patientAlreadyUnderDoctor : Boolean = false
                for (patient in model.currPatients) {
                    if (patient.pid == potentialPatient.pid) {
                        patientAlreadyUnderDoctor = true
                    }
                }
                if (potentialPatient.pid == "-1") {
                    model.currPatient = null
                    model.showDialog = true
                    model.addPatientDialogMessage = "This patient does not exist"
                } else if (patientAlreadyUnderDoctor) {
                    model.patientAlreadyUnderDoctor = true
                    model.currPatient = potentialPatient
                    model.showDialog = true
                    model.addPatientDialogMessage = "This patient is already under your care"
                } else {
                    model.patientAlreadyUnderDoctor = false
                    model.currPatient = potentialPatient
                    model.submitEnabled = true
                }
            }
            AddPatientViewEvent.AddPatientEvent -> {
                val isSuccess: Boolean =
                    model.currPatient?.let { DoctorApi().addPatient(model.did, it.pid) } == true
                if (isSuccess) {
                    model.currPatient?.let { model.currPatients.add(it) }
                    model.addPatientDialogMessage = "Success!"
                    model.showDialog = true
                } else {
                    model.addPatientDialogMessage = "Unfortunately could not add patient"
                    model.showDialog = true
                }
                model.submitEnabled = false
            }
            AddPatientViewEvent.DialogClose -> {
                model.showDialog = false
            }
        }
    }
}
