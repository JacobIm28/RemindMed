//package com.gradle.controller
//
//import com.gradle.apiCalls.Medication
//import com.gradle.apiCalls.Patient
//import com.gradle.ui.views.shared.MedicationViewEvent
//import java.sql.Date
//import java.sql.Time
//
class HomepageController {
//    fun invoke(event: MedicationViewEvent, value: Any?) {
//        when(event) {
//            MedicationViewEvent.MedicationIdEvent -> {
//                val medicationResult = Medication().getMedicationbyName(value as String)
//                println("MEDICATION ID $medicationResult")
//                if (medicationResult != null && medicationResult.has("results")) {
//                    val resultsArray = medicationResult.getAsJsonArray("results")
//                    if (resultsArray.size() > 0) {
//                        model.medicationId = resultsArray[0].asJsonObject["id"].asString
//                        println("MEDICATION ID $model.medicationId")
//                    } else {
//                        println("Results array is empty")
//                    }
//                } else {
//                    println("Medication result is null or does not contain 'results' field")
//                }
//            }
//            MedicationViewEvent.NameEvent -> model.name = value as String
//            MedicationViewEvent.AmountEvent -> model.amount = value as String
//            MedicationViewEvent.StartDateEvent -> model.startDate = value as Date
//            MedicationViewEvent.EndDateEvent -> model.endDate = value as Date
//            MedicationViewEvent.NotesEvent -> model.notes = value as String
//
//            MedicationViewEvent.TimeEvent -> model.times = value as MutableList<Time>
//
//            MedicationViewEvent.AddEvent -> {
//                val med = com.gradle.models.Medication(
//                    model.pid,
//                    model.medicationId,
//                    model.amount,
//                    model.startDate,
//                    model.endDate,
//                    model.name,
//                    model.notes,
//                    model.times
//                )
//                try {
//                    model.successfulAdd = Patient().addMedication(med)
//
//                } catch (e: Exception) {
//                    model.errorMessage = e.message.toString()
//                    model.isError = true
//                }
//            }
//
//            MedicationViewEvent.UpdateEvent -> {
//                val med = com.gradle.models.Medication(
//                    model.pid,
//                    model.medicationId,
//                    model.amount,
//                    model.startDate,
//                    model.endDate,
//                    model.name,
//                    model.notes,
//                    model.times
//                )
//                try {
//                    model.successfulChange = Patient().updateMedication(med)
//                } catch (e: Exception) {
//
//                    model.errorMessage = e.message.toString()
//                    model.isError = true
//                }
//            }
//
//            MedicationViewEvent.RemoveEvent -> {
//                try {
//                    model.successfulRemove = Patient().removeMedication(model.pid, model.medicationId)
//                } catch (e: Exception) {
//                    model.errorMessage = e.message.toString()
//                    model.isError = true
//                }
//            }
//
//        }
//    }
}
//}