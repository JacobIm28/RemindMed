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
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext

import com.gradle.constants.GlobalObjects
import com.gradle.controller.MedicationListController
import com.gradle.apiCalls.PatientApi as PatientApi
import com.gradle.models.Medication
import com.gradle.models.MedicationList
import com.gradle.models.Patient
import com.gradle.ui.components.TitleLarge
import com.gradle.ui.theme.*
import com.gradle.utilities.toFormattedDateString
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import androidx.compose.ui.res.painterResource
import com.example.remindmed.R
import com.gradle.controller.MedicationController
import com.gradle.ui.components.ButtonPrimary
import com.gradle.ui.viewModels.MedicationListViewModel
import com.gradle.ui.viewModels.MedicationViewModel
import com.gradle.utilities.notifications.NotificationUtils.Companion.scheduleNotifications

enum class MedicationListViewEvent {
    MedicationRemove
}

//@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MedicationListScreen(
    pid: String,
    onNavigateToMedicationEntry: (String) -> Unit,
    onNavigateToMedicationEdit: (Medication) -> Unit,
    onNavigateToMedicationInfo: (Medication) -> Unit,
) {
    // TODO: Implement some sort async code rot run this on load, and display a loading screen in the meantime
    val coroutineScope = rememberCoroutineScope()
    var medications by remember { mutableStateOf(mutableListOf<Medication>()) }
    var patient by remember { mutableStateOf(Patient()) }
    var model by remember{ mutableStateOf(MedicationList(medications, patient)) }
    var viewModel by remember{mutableStateOf(MedicationListViewModel(model))}
    var controller by remember{ mutableStateOf(MedicationListController(model)) }

    LaunchedEffect(Unit) {
        medications = PatientApi().getMedicines(pid)
        if (GlobalObjects.type == "patient") {
            patient = GlobalObjects.patient
        } else {
            val patientResult = PatientApi().getPatientbyId(pid)
            println(patientResult)
            if (patientResult.pid != "-1") {
                patient = patientResult
            }
        }

        model = MedicationList(medications, patient)
        viewModel = MedicationListViewModel(model)
        controller = MedicationListController(model)
    }

    AppTheme {
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { onNavigateToMedicationEntry(pid) },
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

                HorizontalDivider()
//                HeadlineLarge("Medications")
                LazyColumn {
                    items(viewModel.medicationList.value) { medication ->
                        MedicationItem(
                            context = LocalContext.current,
                            patient = patient,
                            medication = medication,
                            onRemove = {
                                coroutineScope.launch {
                                    controller.invoke(MedicationListViewEvent.MedicationRemove, medication.medicationId)
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
    onClick: () -> Unit, // Click listener for the entire item
    onNavigateToMedicationEdit: (Medication) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var accepted by remember { mutableStateOf(medication.accepted) }
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

//                painter = painterResource(id = R.drawable.Medicine),
//                contentDescription = "RemindMed Logo",
//                modifier = Modifier
//                    .size(500.dp)
//                    .padding(20.dp)
//            )
            Image(painter = painterResource(R.drawable.medicine), contentDescription = null, Modifier.size(50.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(medication.name, fontWeight = FontWeight.Bold)
                Text(medication.amount, style = MaterialTheme.typography.bodyMedium)
                Text("${medication.startDate} - ${medication.endDate}", style = MaterialTheme.typography.bodyMedium)
                Text("${medication.getFormattedTimes()}", style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(modifier = Modifier.weight(1f))

            if (accepted) {
                Column {
                    IconButton(onClick = { showDialog = true }) { // Set showDialog to true when remove icon clicked
                        Icon(Icons.Filled.Clear, contentDescription = "Delete")
                    }
                    IconButton(onClick = { onNavigateToMedicationEdit(medication) }) {
                        Icon(Icons.Filled.Edit, contentDescription = "Edit")
                    }
                }
            } else {
//                ButtonPrimary(
//                    text = "Accept",
//                    onClick = {
//                        scheduleNotifications(context, patient, medication)
//                        accepted = true
//                        medication.accepted = true
//                        var medicationViewModel = MedicationViewModel(medication)
//                        var medicationController = MedicationController(medication)
//
//                        medicationController.invoke(
//                            MedicationViewEvent.UpdateEvent,
//                            medicationViewModel
//                        )
//                    },
//                    enabled = true
//                )
                Column {
                    IconButton(
                        onClick = { showDialog = true }
                    ) {
                        Icon(Icons.Filled.Clear, contentDescription = "Decline")
                    }
                    IconButton(
                        onClick = {
                            scheduleNotifications(context, patient, medication)
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


//@Preview(showBackground = true)
//@Composable
//fun MedicationListScreenPreview() {
//    MedicationListScreen()
//}
//
