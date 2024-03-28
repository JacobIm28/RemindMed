package com.gradle.ui.views.shared

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkBuilder
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextAlign

import com.gradle.constants.GlobalObjects
import com.gradle.apiCalls.Patient as PatientApi
import com.gradle.constants.NavArguments
import com.gradle.constants.Routes
import com.gradle.models.LoginModel
import com.gradle.models.Medication
import com.gradle.models.Patient
import com.gradle.ui.components.ButtonSecondary
import com.gradle.ui.components.HeadlineLarge
import com.gradle.ui.components.LoadingScreen
import com.gradle.ui.components.TitleLarge
import com.gradle.ui.theme.*
import com.gradle.utilities.toFormattedDateString
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.sql.Date
import java.sql.Time
import java.time.format.DateTimeFormatter

import com.gradle.apiCalls.Medication as MedicationApi

//@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MedicationListScreen(
    pid: String,
    onNavigateToMedicationEntry: () -> Unit,
    onNavigateToMedicationEdit: () -> Unit,
    onNavigateToMedicationInfo: (Medication) -> Unit,
) {
    // TODO: Implement some sort async code rot run this on load, and display a loading screen in the meantime
    val coroutineScope = rememberCoroutineScope()

    var medications by remember {
        mutableStateOf<List<Medication>?>(null)
    }
    var patient by remember {
        mutableStateOf<Patient?>(null)
    }

    fun getPatientById() {
        if (GlobalObjects.type == "patient") {
            patient = GlobalObjects.patient
        } else {
            val patientResult = PatientApi().getPatientbyId(pid)
            println(patientResult)
            if (patientResult.pid != "-1") {
                patient = patientResult
            }
        }
    }

    fun getMedications() {
        val medicationResult = PatientApi().getMedicines(pid)
        medications = medicationResult
    }

    LaunchedEffect(Unit) {
        getPatientById()
        getMedications()
    }

    AppTheme {
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { onNavigateToMedicationEntry() },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Medication")
                }
            },
        ) { padding ->
            Column (
                Modifier
                    .padding(padding)
                    .fillMaxWidth()
            ) {
                TitleLarge("${patient?.name?.substringBefore(" ")}'s Medication")

                HeadlineLarge("Medications")
                
                if (patient == null || medications == null) {
                    LoadingScreen()
                } else if(medications!!.isEmpty()) {
                    Text(
                        "No Medications found",
                        modifier = Modifier.fillMaxSize().wrapContentHeight(),
                        style = androidx.compose.material.MaterialTheme.typography.h6,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = md_theme_dark_onTertiary
                    )
                } else {
                  LazyColumn {
                    items(medications!!) { medication ->
                        MedicationItem(
                            medication = medication,
                            onRemove = {
                                val success = PatientApi().removeMedication(GlobalObjects.patient.pid, medication.medicationId)
                                if (success) { println("Medication Removed")
                                   medications = PatientApi().getMedicines(GlobalObjects.patient.pid).toList()
                                } else {
                                    medications = PatientApi().getMedicines(GlobalObjects.patient.pid).toList()
                                    println("Medication Not Removed")
                                }
                                println(medications)
                            },
                            onClick = {
                                onNavigateToMedicationInfo(medication)
                            },
                            onNavigateToMedicationEdit
                        )
                    }
                  }
                }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MedicationItem(
    medication: Medication,
    onRemove: () -> Unit,
    onClick: () -> Unit, // Click listener for the entire item
    onNavigateToMedicationEdit: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .padding(6.dp)
            .clickable(onClick = onClick), // Apply click listener to the entire item
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        colors = CardColors(
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.primary,
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
            val formatter = DateTimeFormatter.ofPattern("MMMM dd")
            val formattedStartDate = medication.startDate.toFormattedDateString().format(formatter)
            val formattedEndDate = medication.endDate.toFormattedDateString().format(formatter)

            Icon(Icons.Outlined.Info, contentDescription = null, Modifier.size(50.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(medication.name, fontWeight = FontWeight.Bold)
                Text(medication.amount, style = MaterialTheme.typography.bodyMedium)
                Text("${medication.startDate} - ${medication.endDate}", style = MaterialTheme.typography.bodyMedium)
                Text("${medication.times}", style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(modifier = Modifier.weight(1f))

            Column {
                IconButton(onClick = { onNavigateToMedicationEdit() }) {
                    Icon(Icons.Filled.Edit, contentDescription = "Edit")
                }
                // Remove icon
                IconButton(onClick = { showDialog = true }) { // Set showDialog to true when remove icon clicked
                    Icon(Icons.Filled.Delete, contentDescription = "Delete")
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirm Delete") },
            text = { Text("Are you sure you want to delete this medication?") },
            confirmButton = {
                Button(
                    onClick = {
                        onRemove()
                        showDialog = false
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}


//@Preview(showBackground = true)
//@Composable
//fun MedicationListScreenPreview() {
//    MedicationListScreen()
//}
//
