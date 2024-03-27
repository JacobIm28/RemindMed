package com.gradle.ui.views.shared

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.gradle.constants.GlobalObjects
import com.gradle.constants.NavArguments
import com.gradle.constants.Routes
import androidx.compose.runtime.LaunchedEffect
import com.gradle.models.Doctor
import com.gradle.ui.theme.*
import com.gradle.models.Patient
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

@Composable
fun DoctorItem(doctor: Doctor) {
    AppTheme {
        Card(
            modifier = Modifier.padding(6.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            ),

            colors = CardColors(
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                disabledContentColor = MaterialTheme.colorScheme.onTertiaryContainer
            )
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Outlined.Person, contentDescription = null, Modifier.size(50.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(doctor.name, fontWeight = FontWeight.Bold)
                    Text(doctor.email)
                }
            }
        }
    }
}

@Composable
fun PatientItem(patient: Patient, navController: NavController, isValid: Boolean, isAddPatient: Boolean) {
    AppTheme {
        Card(
            modifier = Modifier.padding(6.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            ),

            colors = CardColors(
                containerColor = if (isValid) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                disabledContentColor = MaterialTheme.colorScheme.onTertiaryContainer
            ),
            onClick = {
                if (!isAddPatient) {
                    navController.navigate(Routes.MEDICATION_LIST + "?" + "${NavArguments.MEDICATION_LIST.PID}=${patient.pid}")
                }
            }
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Outlined.Person, contentDescription = null, Modifier.size(50.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(patient.name, fontWeight = FontWeight.Bold)
                    Text(patient.email)
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

