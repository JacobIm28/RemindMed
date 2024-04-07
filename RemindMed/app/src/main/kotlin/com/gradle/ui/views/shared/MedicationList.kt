package com.gradle.ui.views.shared

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cs346.remindmed.R
import com.gradle.apiCalls.PatientApi
import com.gradle.constants.GlobalObjects
import com.gradle.controller.MedicationController
import com.gradle.controller.MedicationListController
import com.gradle.models.Medication
import com.gradle.models.MedicationList
import com.gradle.models.Patient
import com.gradle.ui.components.TitleLarge
import com.gradle.ui.theme.*
import com.gradle.ui.viewModels.MedicationListViewModel
import com.gradle.ui.viewModels.MedicationViewModel
import com.gradle.utilities.notifications.NotificationUtils.Companion.scheduleNotifications
import kotlinx.coroutines.launch
import java.sql.Time

enum class MedicationListViewEvent {
    MedicationRemove
}

@RequiresApi(Build.VERSION_CODES.S)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MedicationListScreen(
    pid: String,
    onNavigateToMedicationEntry: (String) -> Unit,
    onNavigateToMedicationEdit: (Medication) -> Unit,
    onNavigateToMedicationInfo: (Medication) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    var medications by remember { mutableStateOf(mutableListOf<Medication>()) }
    var patient by remember { mutableStateOf(Patient()) }
    var model by remember { mutableStateOf(MedicationList(medications, patient)) }
    var viewModel by remember { mutableStateOf(MedicationListViewModel(model)) }
    var controller by remember { mutableStateOf(MedicationListController(model)) }
    var name by remember { mutableStateOf("") }

    println("PID: $pid")
    LaunchedEffect(Unit) {
        medications = PatientApi().getMedicines(pid)
        if (GlobalObjects.type == "patient") {
            patient = GlobalObjects.patient
        } else {
            val patientResult = PatientApi().getPatientbyId(pid)
            if (patientResult.pid != "-1") {
                patient = patientResult
            }
            println(patient)
        }

        model = MedicationList(medications, patient)
        viewModel = MedicationListViewModel(model)
        controller = MedicationListController(model)
    }

    LaunchedEffect(patient) {
        name = patient.name
        println("Name: $name")
    }

    AppTheme {
        Scaffold(
            floatingActionButton = {
                if (GlobalObjects.type == "doctor") {
                    FloatingActionButton(
                        onClick = { onNavigateToMedicationEntry(pid) },
                        containerColor = MaterialTheme.colorScheme.primary
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add Medication")
                    }
                }
            },
        ) { padding ->
            Column(
                Modifier
                    .padding(padding)
                    .fillMaxWidth()
            ) {
                TitleLarge("${name.substringBefore(" ")}'s Medication")
                if(viewModel.medicationList.value.isEmpty()) {
                    Text(
                        "No Medications Found",
                        modifier = Modifier.fillMaxSize().wrapContentHeight(),
                        style = androidx.compose.material.MaterialTheme.typography.h6,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                HorizontalDivider()
                LazyColumn {
                    items(viewModel.medicationList.value) { medication ->
                        Spacer(modifier = Modifier.height(8.dp))
                        MedicationItem(
                            context = LocalContext.current,
                            patient = patient,
                            medication = medication,
                            onRemove = {
                                coroutineScope.launch {
                                    controller.invoke(
                                        MedicationListViewEvent.MedicationRemove,
                                        medication
                                    )
                                }
                            },
                            onClick = { onNavigateToMedicationInfo(medication) },
                            onNavigateToMedicationEdit
                        )
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun MedicationItem(
    context: Context,
    patient: Patient,
    medication: Medication,
    onRemove: () -> Unit,
    onClick: () -> Unit,
    onNavigateToMedicationEdit: (Medication) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var accepted by remember { mutableStateOf(medication.accepted) }
    Card(
        modifier = Modifier
            .padding(horizontal = 6.dp, vertical = 3.dp) // Adjust padding here
            .clickable(onClick = onClick), // Apply click listener to the entire item
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        shape = RoundedCornerShape(16.dp), // Adjust shape here
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
            Image(
                painter = painterResource(R.drawable.medicine),
                contentDescription = null,
                Modifier.size(50.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(medication.name, fontWeight = FontWeight.Bold)

                Text("Dosage: ${medication.amount}", style = MaterialTheme.typography.bodyMedium)

                Text(
                    "Dates: ${medication.startDate} - ${medication.endDate}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    "Times: ${medication.getFormattedTimes().joinToString(", ")}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text("Notes: ${medication.notes}", style = MaterialTheme.typography.bodyMedium)
            }

            Column {
                if (accepted || GlobalObjects.type == "doctor") {
                    IconButton(onClick = {
                        showDialog = true
                    }) {
                        Icon(Icons.Filled.Clear, contentDescription = "Delete")
                    }
                    IconButton(onClick = { onNavigateToMedicationEdit(medication) }) {
                        Icon(Icons.Filled.Edit, contentDescription = "Edit")
                    }
                } else if (GlobalObjects.type == "patient") {
                    IconButton(
                        onClick = { showDialog = true }
                    ) {
                        Icon(Icons.Filled.Clear, contentDescription = "Decline")
                    }
                    IconButton(
                        onClick = {
                            scheduleNotifications(
                                context,
                                patient,
                                medication,
                                mutableListOf<Time>()
                            )
                            accepted = true
                            medication.accepted = true
                            var medicationViewModel = MedicationViewModel(medication)
                            var medicationController = MedicationController(medication)

                            medicationController.invoke(
                                MedicationViewEvent.UpdateEvent,
                                medicationViewModel
                            )
                        }) {
                        Icon(Icons.Filled.Check, contentDescription = "Accept")
                    }
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
