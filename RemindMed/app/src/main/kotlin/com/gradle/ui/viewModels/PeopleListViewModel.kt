package com.gradle.ui.viewModels

import androidx.compose.runtime.mutableStateOf
import com.gradle.models.PeopleList

class PeopleListViewModel(val model : PeopleList) : ISubscriber {
    var patientList = mutableStateOf(model.patientList)
    var doctorList = mutableStateOf(model.doctorList)
    var successfullyRemovedPatient = mutableStateOf(model.successfullyRemovedPatient)
    var showDialog = mutableStateOf(model.showDialog)

    init {
        model.subscribe(this)
    }

    override fun update() {
        patientList.value = model.patientList
        doctorList.value = model.doctorList
        successfullyRemovedPatient.value = model.successfullyRemovedPatient
        showDialog.value = model.showDialog
    }
}