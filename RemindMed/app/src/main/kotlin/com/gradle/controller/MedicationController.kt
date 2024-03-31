package com.gradle.controller

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.mutableStateListOf
import com.gradle.constants.GlobalObjects
import com.gradle.models.Medication
import com.gradle.ui.views.shared.MedicationViewEvent
import java.sql.Date
import java.sql.Time

import com.gradle.apiCalls.Patient as PatientApi
import com.gradle.apiCalls.Medication as MedicationApi

class MedicationController(val model: Medication) {
//    @OptIn(ExperimentalMaterial3Api::class)
//    fun getSelectedTimes(timeStates: MutableList<TimePickerState>): MutableList<Time> {
//        val selectedTimes = mutableListOf<Time>()
//        for (timeState in timeStates) {
//            selectedTimes.add(Time(timeState.hour, timeState.minute, 0))
//        }
//        print("SELECTED TIMES: $selectedTimes")
//        return selectedTimes
//    }
    @OptIn(ExperimentalMaterial3Api::class)
    fun invoke(event: MedicationViewEvent, value: Any?) {
        when(event) {
            MedicationViewEvent.MedicationIdEvent -> {
                val medicationResult = MedicationApi().getMedicationbyName(value as String)
                println("MEDICATION ID $medicationResult")
                if (medicationResult != null && medicationResult.has("results")) {
                    val resultsArray = medicationResult.getAsJsonArray("results")
                    if (resultsArray.size() > 0) {
                        model.medicationId = resultsArray[0].asJsonObject["id"].asString
                        println("MEDICATION ID $model.medicationId")
                    } else {
                        println("Results array is empty")
                    }
                } else {
                    println("Medication result is null or does not contain 'results' field")
                }
            }
            MedicationViewEvent.NameEvent -> model.name = value as String
            MedicationViewEvent.AmountEvent -> model.amount = value as String
            MedicationViewEvent.StartDateEvent -> model.startDate = value as Date
            MedicationViewEvent.EndDateEvent -> model.endDate = value as Date
            MedicationViewEvent.NotesEvent -> model.notes = value as String

            MedicationViewEvent.TimeEvent -> model.times = value as MutableList<Time>

            MedicationViewEvent.AddEvent -> {
                val med = Medication(model.pid, model.medicationId, model.amount, model.startDate, model.endDate, model.name, model.notes, model.times)
                try {
                    model.successfulAdd = PatientApi().addMedication(med)

                } catch (e: Exception) {
                    model.errorMessage = e.message.toString()
                    model.isError = true
                }
            }

            MedicationViewEvent.UpdateEvent -> {
                val med = Medication(model.pid, model.medicationId, model.amount, model.startDate, model.endDate, model.name, model.notes, model.times)
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
