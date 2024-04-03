package com.gradle.models

class AddPatient(var _did: String, var _currPatients: MutableList<Patient>) : IPresenter() {
    val did : String = _did

    var email: String = ""
        set(value) {
            field = value
            notifySubscribers()
        }

    var emailIsValid : Boolean = true
        set(value) {
            field = value
            notifySubscribers()
        }

    var currPatients : MutableList<Patient> = _currPatients
        set(value) {
            field = value
            notifySubscribers()
        }

    var showPotentialPatient : Boolean = false
        set(value) {
            field = value
            notifySubscribers()
        }

    var currPatient : Patient? = null
        set(value) {
            field = value
            notifySubscribers()
        }

    var patientAlreadyUnderDoctor = false
        set(value) {
            field = value
            notifySubscribers()
        }

    var showDialog = false
        set(value) {
            field = value
            notifySubscribers()
        }

    var addPatientDialogMessage = ""
        set(value) {
            field = value
            notifySubscribers()
        }

    var submitEnabled = false
        set(value) {
            field = value
            notifySubscribers()
        }
}