package com.gradle.models

class PeopleList : IPresenter() {
    var patientList: MutableList<Patient> = mutableListOf<Patient>()
        set(value) {
            field = value
            notifySubscribers()
        }

    var doctorList: MutableList<Doctor> = mutableListOf<Doctor>()
        set(value) {
            field = value
            notifySubscribers()
        }

    var successfullyRemovedPatient = false
        set(value) {
            field = value
            notifySubscribers()
        }

    var showDialog = false
        set(value) {
            field = value
            notifySubscribers()
        }
}