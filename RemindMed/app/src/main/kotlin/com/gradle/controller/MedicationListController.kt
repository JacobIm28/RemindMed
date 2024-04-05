package com.gradle.controller

import android.os.Build
import androidx.annotation.RequiresApi
import com.gradle.constants.GlobalObjects
import com.gradle.models.Medication
import com.gradle.models.MedicationList
import com.gradle.ui.views.shared.MedicationListViewEvent
import com.gradle.apiCalls.PatientApi as PatientApi

class MedicationListController(val model: MedicationList) {
    @RequiresApi(Build.VERSION_CODES.O)
    fun invoke(event: MedicationListViewEvent, value: Medication) {
        when(event) {
            MedicationListViewEvent.MedicationRemove -> {
                val success = PatientApi().removeMedication(value.pid, value.medicationId)
                if (success) {
                    model.medicationList = PatientApi().getMedicines(value.pid).toList()
                }
            }
        }
    }
}