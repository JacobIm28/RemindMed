package com.gradle.controller

import android.os.Build
import androidx.annotation.RequiresApi
import com.gradle.constants.GlobalObjects
import com.gradle.models.MedicationList
import com.gradle.ui.views.shared.MedicationListViewEvent
import com.gradle.apiCalls.Patient as PatientApi

class MedicationListController(val model: MedicationList) {
    @RequiresApi(Build.VERSION_CODES.O)
    fun invoke(event: MedicationListViewEvent, value: Any?) {
        when(event) {
            MedicationListViewEvent.MedicationRemove -> {
                val medicationId : String = value.toString()
                val success = PatientApi().removeMedication(GlobalObjects.patient.pid, medicationId)
                if (success) {
                    model.medicationList = PatientApi().getMedicines(GlobalObjects.patient.pid).toList()
                } else {
                    println("Medication Not Removed")
                }
            }
        }
    }
}