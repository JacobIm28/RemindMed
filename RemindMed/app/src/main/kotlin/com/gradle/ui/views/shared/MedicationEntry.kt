package com.gradle.ui.views.shared

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.gradle.apiCalls.MedicationApi
import com.gradle.apiCalls.PatientApi
import com.gradle.constants.GlobalObjects
import com.gradle.controller.MedicationController
import com.gradle.models.Medication
import com.gradle.models.Patient
import com.gradle.ui.components.ButtonSecondary
import com.gradle.ui.components.CustomDatePicker
import com.gradle.ui.components.CustomTimePicker
import com.gradle.ui.components.MedicationSummaryCard
import com.gradle.ui.components.TitleLarge
import com.gradle.ui.theme.AppTheme
import com.gradle.ui.viewModels.MedicationViewModel
import com.gradle.utilities.notifications.NotificationUtils.Companion.scheduleNotifications
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.sql.Date
import java.sql.Time
import java.util.concurrent.TimeUnit

enum class MedicationViewEvent {
    MedicationIdEvent,
    NameEvent,
    AmountEvent,
    StartDateEvent,
    EndDateEvent,
    TimeEvent,
    NotesEvent,
    AddEvent,
    UpdateEvent,
    RemoveEvent,
    AcceptedEvent,
    TakenEvent
}

@RequiresApi(Build.VERSION_CODES.S)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@SuppressLint("RememberReturnType", "UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MedicationEntryScreen(
    onNavigateToPeopleList: () -> Unit,
    onNavigateToMedicationList: (String) -> Unit,
    medicationViewModel: MedicationViewModel,
    medicationController: MedicationController,
    pid: String
) {
    val context = LocalContext.current

    var medications by remember { mutableStateOf(emptyList<Medication>()) }
    var user by remember { mutableStateOf<Patient?>(null) }

    LaunchedEffect(Unit) {
        user = PatientApi().getPatientbyId(pid)
        medications = PatientApi().getMedicines(pid)
        medicationController.model.pid = pid
        medicationViewModel.clearTimePickerState()
    }

    val controller by remember { mutableStateOf(medicationController) }

    val showAddMedicationErrorDialog = remember { mutableStateOf(false) }

    val showValidationErrorDialog = remember { mutableStateOf(false) }

    val showDuplicateMedicationErrorDialog = remember { mutableStateOf(false) }

    val showDateInvalidRangeErrorDialog = remember { mutableStateOf(false) }

    if (showAddMedicationErrorDialog.value) {
        AlertDialog(
            onDismissRequest = { showAddMedicationErrorDialog.value = false },
            title = { Text("Error") },
            text = { Text("Failed to add medication. Please try again.") },
            confirmButton = {
                Button(onClick = { showAddMedicationErrorDialog.value = false }) {
                    Text("OK")
                }
            }
        )
    }

    if (showValidationErrorDialog.value) {
        AlertDialog(
            onDismissRequest = { showValidationErrorDialog.value = false },
            title = { Text("Validation Error") },
            text = { Text("Please check your inputs. Medication Name, Dates, and Time fields are required and must be valid.") },
            confirmButton = {
                Button(onClick = { showValidationErrorDialog.value = false }) {
                    Text("OK")
                }
            }
        )
    }

    if (showDuplicateMedicationErrorDialog.value) {
        AlertDialog(
            onDismissRequest = { showDuplicateMedicationErrorDialog.value = false },
            title = { Text("Duplicate Medication") },
            text = { Text("This medication already exists as a prescription in your medication list. Please check your inputs.") },
            confirmButton = {
                Button(onClick = { showDuplicateMedicationErrorDialog.value = false }) {
                    Text("OK")
                }
            }
        )
    }

    if (showDateInvalidRangeErrorDialog.value) {
        AlertDialog(
            onDismissRequest = { showDateInvalidRangeErrorDialog.value = false },
            title = { Text("Date Error") },
            text = { Text("The start date must be before or equal to the end date.") },
            confirmButton = {
                Button(onClick = { showDateInvalidRangeErrorDialog.value = false }) {
                    Text("OK")
                }
            }
        )
    }

    var startDateState = rememberDatePickerState()

    var endDateState = rememberDatePickerState()

    fun validateInputs(
    ): Boolean {
        return medicationViewModel.name.value.isNotBlank() &&
                medicationViewModel.startDate.value != null &&
                medicationViewModel.endDate.value != null &&
                medicationViewModel.getSelectedTimes().isNotEmpty()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun duplicateMedication(): Boolean {
        for (medication in medications) {
            if (medication.name == medicationViewModel.name.value) {
                return true
            }
        }
        return false
    }

    var searchResults by remember { mutableStateOf<List<Pair<String, String>>>(emptyList()) }
    var searchTerm by remember { mutableStateOf(TextFieldValue("")) }

    val scope = CoroutineScope(Dispatchers.Main)
    var searchJob: Job? = null

    @Composable
    fun TimeInput(medicationViewModel: MedicationViewModel) {
        Column {
            Text(
                text = "Select Times",
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, color = Color.Gray, shape = RoundedCornerShape(5.dp))
                    .padding(8.dp)
            ) {
                Column {
                    medicationViewModel.timeStates.forEachIndexed { index, timeState ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CustomTimePicker(medicationViewModel, timeState, false)

                            Spacer(modifier = Modifier.width(8.dp))
                            if (medicationViewModel.timeStates.size > 1) {
                                IconButton(
                                    onClick = {
                                        medicationViewModel.removeTimePickerState(index)
                                    },
                                    modifier = Modifier.padding(start = 4.dp)
                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = "Remove Time")
                                }
                            }
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier.size(30.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .background(
                                        MaterialTheme.colorScheme.primary,
                                        shape = RoundedCornerShape(5.dp)
                                    )
                            )
                            IconButton(
                                onClick = {
                                    medicationViewModel.addTimePickerState()
                                },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    Icons.Default.Add,
                                    contentDescription = "Add Time",
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.S)
    @Composable
    fun MedicationSearchBar(
        onSearch: () -> Unit,
        suggestions: List<Pair<String, String>>
    ) {
        var expanded by remember { mutableStateOf(false) }

        var suggestionClicked by remember { mutableStateOf(false) }

        fun searchWithDelay(term: TextFieldValue) {
            searchJob?.cancel()
            searchJob = scope.launch {
                delay(300)
                onSearch()
            }
        }

        Column {
            OutlinedTextField(
                value = medicationViewModel.name.value,
                onValueChange = { term ->
                    medicationViewModel.name.value = term
                    if (term.isNotBlank()) {
                        searchWithDelay(TextFieldValue(term))
                    } else {
                        searchResults = emptyList()
                    }
                    suggestionClicked = false
                    expanded =
                        (term.isNotBlank() && !suggestionClicked) // Expand dropdown only when there's text
                },
                label = { Text("Search Medication") },
                placeholder = { Text("Enter Medication Name") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(Icons.Filled.ArrowDropDown, contentDescription = "Expand")
                    }
                }
            )

            if (expanded && suggestions.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 200.dp)
                        .background(Color.White)
                        .border(1.dp, Color.Gray)
                        .zIndex(1f)
                ) {
                    LazyColumn {
                        items(suggestions) { suggestion ->
                            Text(
                                text = suggestion.first,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        suggestionClicked = true
                                        controller.invoke(
                                            MedicationViewEvent.NameEvent,
                                            suggestion.first
                                        )

                                        controller.invoke(
                                            MedicationViewEvent.MedicationIdEvent,
                                            suggestion
                                        )
                                        expanded = false
                                    }
                                    .padding(vertical = 16.dp, horizontal = 16.dp),
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Black,
                                textAlign = TextAlign.Start

                            )
                            HorizontalDivider(color = Color.Gray)
                        }
                    }
                }
            }
        }
        DisposableEffect(Unit) {
            onDispose {
                suggestionClicked = false
            }
        }
    }

    AppTheme {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            TitleLarge("Enter Medication")
            Spacer(modifier = Modifier.height(16.dp))

            MedicationSearchBar(
                onSearch = {
                    searchResults = MedicationApi().getAllMedicationsbyName(medicationViewModel.name.value)
                },
                suggestions = searchResults
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = medicationViewModel.amount.value,
                onValueChange = {
                    controller.invoke(MedicationViewEvent.AmountEvent, it)
                },
                label = { Text("Dosage") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Column(
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Text(
                        text = "Select Dates",
                        textAlign = TextAlign.Center
                    )
                }

                CustomDatePicker(startDateState, "Start Date") {
                    controller.invoke(
                        MedicationViewEvent.StartDateEvent,
                        Date(startDateState.selectedDateMillis?.let { it + TimeUnit.DAYS.toMillis(1) }
                            ?: System.currentTimeMillis()))

                }
                CustomDatePicker(endDateState, "End Date") {
                    controller.invoke(
                        MedicationViewEvent.EndDateEvent,
                        Date(endDateState.selectedDateMillis?.let { it + TimeUnit.DAYS.toMillis(1) }
                            ?: System.currentTimeMillis()))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            TimeInput(medicationViewModel)

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = medicationViewModel.notes.value,
                onValueChange = {
                    controller.invoke(MedicationViewEvent.NotesEvent, it)
                },
                label = { Text("Notes") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(30.dp))

            MedicationSummaryCard(
                name = medicationViewModel.name.value,
                dosage = medicationViewModel.amount.value,
                time = if (medicationViewModel.getSelectedFormattedTimes().isEmpty()) {
                    ""
                } else {
                    "${medicationViewModel.getSelectedFormattedTimes()}"
                },
                dates = if (medicationViewModel.startDate.value == Date(0) && medicationViewModel.endDate.value == Date(
                        0
                    )
                ) {
                    ""
                } else {
                    "${medicationViewModel.startDate.value} - ${medicationViewModel.endDate.value}"
                },
                specifications = medicationViewModel.notes.value,
            )
            Spacer(modifier = Modifier.height(30.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ButtonSecondary(
                    text = "Cancel",
                    onClick = {
                        if (GlobalObjects.type == "doctor") {
                            onNavigateToPeopleList()
                        } else {
                            onNavigateToMedicationList(pid)
                        }
                    },
                    enabled = true
                )

                ButtonSecondary(
                    text = "Add",
                    onClick = {
                        if (validateInputs()) {
                            if (duplicateMedication()) {
                                showDuplicateMedicationErrorDialog.value = true
                            } else {

                                val startDate = medicationViewModel.startDate.value
                                val endDate = medicationViewModel.endDate.value

                                if (startDate > endDate) {
                                    showDateInvalidRangeErrorDialog.value = true
                                } else {
                                    controller.invoke(
                                        MedicationViewEvent.TimeEvent,
                                        medicationViewModel.getSelectedTimes()
                                    )
                                    medicationViewModel.model.accepted =
                                        GlobalObjects.type == "patient"
                                    controller.invoke(
                                        MedicationViewEvent.AddEvent,
                                        medicationViewModel
                                    )

                                    if (medicationViewModel.successfulAdd.value) {
                                        if (GlobalObjects.type == "patient") {
                                            scheduleNotifications(
                                                context,
                                                user!!,
                                                medicationViewModel.model,
                                                mutableListOf<Time>()
                                            )
                                            onNavigateToMedicationList(GlobalObjects.patient.pid)
                                        } else {
                                            onNavigateToMedicationList(medicationViewModel.pid.value)
                                        }
                                        onNavigateToMedicationList(pid)
                                        medicationViewModel.clearAll()
                                    } else {
                                        showAddMedicationErrorDialog.value = true
                                    }
                                }
                            }
                        } else {
                            showValidationErrorDialog.value = true
                        }
                    },
                    enabled = true
                )
            }

        }
    }
}

