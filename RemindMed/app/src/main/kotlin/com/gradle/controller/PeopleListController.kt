package com.gradle.controller

import com.gradle.constants.GlobalObjects
import com.gradle.models.PeopleList
import com.gradle.ui.views.shared.PeopleListEvent
import com.gradle.apiCalls.DoctorApi

class PeopleListController(val model : PeopleList) {
    fun invoke(event: PeopleListEvent, value: Any?) {
        when (event) {
            PeopleListEvent.DeleteEvent -> {
                model.successfullyRemovedPatient = DoctorApi().removePatient(GlobalObjects.doctor.did, value.toString())
                
                if (model.successfullyRemovedPatient) {
                    model.patientList = DoctorApi().getPatients(GlobalObjects.doctor.did)
                }

                model.showDialog = true
            }
        }
    }
}