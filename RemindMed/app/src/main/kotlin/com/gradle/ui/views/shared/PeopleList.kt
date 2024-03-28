package com.gradle.ui.views.shared

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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
    var patientList: MutableList<Patient> by remember { mutableStateOf(mutableListOf<Patient>()) }
    var doctorList: MutableList<Doctor> by remember { mutableStateOf(mutableListOf<Doctor>()) }
    LaunchedEffect(Unit) {
        if (GlobalObjects.type == "doctor") {
            patientList = DoctorApi().getPatients(GlobalObjects.doctor.did)
        } else {
            doctorList = PatientApi().getDoctors(GlobalObjects.patient.pid)
        }
    }

    AppTheme {
        LazyColumn(modifier = Modifier.padding()) {
            if (GlobalObjects.type == "doctor") {
                if (patientList.isEmpty()) {
                    item {
                        Text(
                            "No patients found",
                            modifier = Modifier.fillMaxSize().wrapContentHeight(),
                            style = typography.h6,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            color = md_theme_dark_onTertiary
                        )
                    }
                }
                items(patientList) { patient ->
                    PatientItem(patient, navController, true, false)
                }
            } else {
                if (doctorList.isEmpty()) {
                    item {
                        Text(
                            "No doctors found",
                            modifier = Modifier.fillMaxSize().wrapContentHeight(),
                            style = typography.h6,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            color = md_theme_dark_onTertiary
                        )
                    }
                }
                items(doctorList) { doctor ->
                    DoctorItem(doctor)
                }
            }
        }
    }
}


