package com.gradle.ui.viewModels

import androidx.compose.runtime.mutableStateOf
import com.gradle.models.MedicationList

class MedicationListViewModel(val model: MedicationList) : ISubscriber {
    val medicationList = mutableStateOf(model.medicationList)
    val patient = mutableStateOf(model.patient)

    init {
        model.subscribe(this)
    }

    override fun update() {
        medicationList.value = model.medicationList
        patient.value = model.patient
    }

}