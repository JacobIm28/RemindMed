package com.gradle.ui.views.shared

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.gradle.constants.GlobalObjects
import androidx.compose.runtime.LaunchedEffect
import com.gradle.models.Doctor
import com.gradle.ui.theme.*
import com.gradle.models.Patient
import com.gradle.ui.components.DoctorItem
import com.gradle.ui.components.PatientItem
import com.gradle.apiCalls.Patient as PatientApi
import com.gradle.apiCalls.Doctor as DoctorApi

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun PeopleListScreen(navController: NavController) {
    var patientList: MutableList<Patient> by remember {mutableStateOf(mutableListOf<Patient>())}
    var doctorList : MutableList<Doctor> by remember { mutableStateOf(mutableListOf<Doctor>()) }
    LaunchedEffect(Unit) {
        if (GlobalObjects.type == "doctor") {
            patientList = DoctorApi().getPatients(GlobalObjects.doctor.did)
        } else {
            doctorList = PatientApi().getDoctors(GlobalObjects.patient.pid)
        }
    }

    AppTheme {
        LazyColumn (modifier = Modifier.padding()) {
            if (GlobalObjects.type == "doctor") {
                items(patientList) {patient ->
                    PatientItem(patient as Patient, navController, true, false)
                }
            } else {
                items (doctorList) { doctor ->
                    DoctorItem(doctor as Doctor)
                }
            }
        }
    }
}


