package com.gradle.models

class MedicationList(var _medicationList : List<Medication> , var _patient: Patient) : IPresenter() {
    var medicationList : List<Medication> = _medicationList
        set(value) {
            field = value
            notifySubscribers()
        }

    var patient : Patient = _patient
        set(value) {
            field = value
            notifySubscribers()
        }
}