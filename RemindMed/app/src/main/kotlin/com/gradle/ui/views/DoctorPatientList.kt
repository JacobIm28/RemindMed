package com.gradle.ui.views

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
import com.gradle.ui.theme.*
import io.ktor.http.content.LastModifiedVersion

// Patient object that I will have for now that will later be pulled from DB
data class Patient(val name: String, val age: Int, val Gender: String)

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun DoctorPatientListScreen(navController: NavController) {
    val patients = listOf(
        Patient("Gen", 20, "Male"),
        Patient("Jacob", 20, "Male"),
        Patient("Samir", 20, "Male"),
        Patient("Jason", 21, "Male"),
    )

    AppTheme {
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    // needs to route to the page for doctors to add patients later
                    // onClick = { navController.navigate(Routes.USER_MEDICATION_ENTRY)},
                    onClick = {},
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Medication")
                }
            },
        ) { padding ->
            Column (
                Modifier
                    .padding(padding)
            ) {
                TitleLarge("Patients")

                LazyColumn {
                    items(patients) { patient ->
                        PatientItem(patient)
                    }
                }
            }
        }
    }
}

@Composable
fun PatientItem(patient: Patient) {
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
            IconButton(onClick = { /* TODO: Handle navigate to patient details */ }) {
                Icon(Icons.Default.ArrowForward, contentDescription = "Go to details")
            }
        }
    }
}

