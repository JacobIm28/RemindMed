package com.gradle.ui.views.shared

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.navArgument
import com.gradle.constants.GlobalObjects
import com.gradle.constants.NavArguments
import com.gradle.constants.Routes
import com.gradle.constants.doctorView
import com.gradle.models.Doctor
import com.gradle.models.Medication
//import com.gradle.ui.components.notifications.NotificationService
import com.gradle.ui.theme.*
import com.gradle.models.Patient
import com.gradle.ui.components.TitleLarge
import com.gradle.apiCalls.Patient as PatientApi
import com.gradle.apiCalls.Doctor as DoctorApi

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun PeopleListScreen(navController: NavController) {
    var peopleList = if(GlobalObjects.type == "patient") {
        PatientApi().getDoctors(GlobalObjects.patient.pid)
    } else {
        DoctorApi().getPatients(GlobalObjects.doctor.did)
    }
    val doctorView = if(GlobalObjects.type == "patient") {
        false
    } else {
        true
    }

    AppTheme {
        Scaffold(
//            floatingActionButton = {
//                if (doctorView) {
//                    FloatingActionButton(
//                        onClick = { navController.navigate(Routes.ADD_PATIENT) },
//                        containerColor = MaterialTheme.colorScheme.primary
//                    ) {
//                        Icon(Icons.Default.Add, contentDescription = "Add Medication")
//                    }
//                }
//            },
        ) { padding ->
            Column (
                Modifier
                    .padding(padding)
            ) {
                if (doctorView) {
                    TitleLarge("Patients")
                } else {
                    TitleLarge("Doctors")
                }

                if (doctorView) {
                    LazyColumn {
                        items(peopleList) { patient ->
                            PatientItem(patient as Patient, navController, true, false)
                        }
                    }
                } else {
                    LazyColumn {
                        items(peopleList) { doctor ->
                            DoctorItem(doctor as Doctor)
                        }
                    }
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
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
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
                Icon(Icons.Outlined.Info, contentDescription = null, Modifier.size(50.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(doctor.name, fontWeight = FontWeight.Bold)
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
                containerColor = if (isValid) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.tertiaryContainer,
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
                    Text(patient.name, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.weight(1f))
                if (!isAddPatient){
                    IconButton(onClick = {
                        navController.navigate(Routes.MEDICATION_LIST + "?" +
                                "${NavArguments.MEDICATION_LIST.PID}=${patient.pid}")
                    }) {
                        Icon(Icons.Default.ArrowForward, contentDescription = "Go to details")
                    }
                }
            }
        }
    }
}

