package com.gradle.ui.views.shared

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.gradle.constants.Routes
import com.gradle.constants.doctorView
import com.gradle.ui.theme.*

// Patient object that I will have for now that will later be pulled from DB
data class Patient(val name: String, val age: Int, val Gender: String)

data class Doctor(val name: String, val practice: String)

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun PeopleListScreen(navController: NavController) {
    val patients = listOf(
        Patient("Gen", 20, "Male"),
        Patient("Jacob", 20, "Male"),
        Patient("Samir", 20, "Male"),
        Patient("Jason", 21, "Male"),
    )

    val doctors = listOf(
        Doctor("Dough Kavanagh", "Family doctor")
    )

    AppTheme {
        Scaffold(
            floatingActionButton = {
                if (doctorView) {
                    FloatingActionButton(
                        onClick = { navController.navigate(Routes.ADD_PATIENT) },
                        containerColor = MaterialTheme.colorScheme.primary
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add Medication")
                    }
                }
            },
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
                        items(patients) { patient ->
                            PatientItem(patient, navController)
                        }
                    }
                } else {
                    LazyColumn {
                        items(doctors) { doctor ->
                            DoctorItem(doctor)
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
fun PatientItem(patient: Patient, navController: NavController) {
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
                    Text(patient.name, fontWeight = FontWeight.Bold)
                    Text("Age: ${patient.age}")
                    Text("Gender: ${patient.age}")
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = {
//                    currName = patient.name
                    navController.navigate(Routes.MEDICATION_LIST)
                }) {
                    Icon(Icons.Default.ArrowForward, contentDescription = "Go to details")
                }
            }
        }
    }
}

