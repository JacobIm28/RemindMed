package com.gradle.controller

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.ExperimentalMaterial3Api
import com.gradle.models.Medication
import com.gradle.ui.views.shared.MedicationViewEvent
import java.sql.Date
import java.sql.Time

import com.gradle.apiCalls.PatientApi as PatientApi
import com.gradle.apiCalls.MedicationApi as MedicationApi

class MedicationController(val model: Medication) {
    @RequiresApi(Build.VERSION_CODES.S)
    @OptIn(ExperimentalMaterial3Api::class)
    fun invoke(event: MedicationViewEvent, value: Any?) {
        when(event) {
            MedicationViewEvent.MedicationIdEvent -> {
                val medicationResult = MedicationApi().getMedicationbyName(value as String)
                if (medicationResult != null && medicationResult.has("results")) {
                    val resultsArray = medicationResult.getAsJsonArray("results")
                    if (resultsArray.size() > 0) {
                        model.medicationId = resultsArray[0].asJsonObject["id"].asString
                    }
                }
            }
            MedicationViewEvent.NameEvent -> model.name = value as String
            MedicationViewEvent.AmountEvent -> model.amount = value as String
            MedicationViewEvent.StartDateEvent -> model.startDate = value as Date
            MedicationViewEvent.EndDateEvent -> model.endDate = value as Date
            MedicationViewEvent.NotesEvent -> model.notes = value as String
            MedicationViewEvent.TimeEvent -> model.times = value as MutableList<Time>
            MedicationViewEvent.AcceptedEvent -> model.accepted = value as Boolean
            MedicationViewEvent.TakenEvent -> model.taken = value as Boolean

            MedicationViewEvent.AddEvent -> {
                val med = Medication(model.pid, model.medicationId, model.amount, model.startDate, model.endDate, model.name, model.notes, model.times, model.accepted, model.taken)
                try {
                    model.successfulAdd = PatientApi().addMedication(med)

                } catch (e: Exception) {
                    model.errorMessage = e.message.toString()
                    model.isError = true
                }
            }

            MedicationViewEvent.UpdateEvent -> {
                val med = Medication(model.pid, model.medicationId, model.amount, model.startDate, model.endDate, model.name, model.notes, model.times, model.accepted, model.taken)
                try {
                    model.successfulChange = PatientApi().updateMedication(med)
                } catch (e: Exception) {

                    model.errorMessage = e.message.toString()
                    model.isError = true
                }
            }

            MedicationViewEvent.RemoveEvent -> {
                try {
                    model.successfulRemove = PatientApi().removeMedication(model.pid, model.medicationId)
                } catch (e: Exception) {
                    model.errorMessage = e.message.toString()
                    model.isError = true
                }
            }

        }
    }
}
