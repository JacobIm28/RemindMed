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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll

import com.gradle.models.Patient

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.gradle.apiCalls.Patient as PatientApi
import com.gradle.constants.NavArguments
import com.gradle.constants.Routes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import com.gradle.constants.GlobalObjects
import com.gradle.ui.components.ButtonSecondary
import com.gradle.ui.components.CustomDatePicker
import com.gradle.ui.components.CustomTimePicker
import com.gradle.ui.components.MedicationSummaryCard
import com.gradle.ui.components.TitleLarge
import com.gradle.ui.theme.AppTheme
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.sql.Date
import java.sql.Time

import java.time.LocalTime
import com.gradle.apiCalls.Medication as MedicationApi
import com.gradle.models.Medication as Medication

import com.gradle.controller.MedicationController
import com.gradle.controller.PatientController
import com.gradle.ui.views.MedicationViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.concurrent.TimeUnit


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@SuppressLint("RememberReturnType", "UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MedicationEditScreen(
    medicationId: String,
    onNavigateToPeopleList: () -> Unit,
    onNavigateToMedicationList: (String) -> Unit,
    medicationViewModel: MedicationViewModel,
    medicationController: MedicationController
) {
//    val coroutineScope = rememberCoroutineScope()

    var medication by remember { mutableStateOf<Medication?>(null) }

    var medications by remember { mutableStateOf(emptyList<Medication>()) }
//    var patient by remember { mutableStateOf(Patient("")) }

    LaunchedEffect(Unit) {
//        patient = PatientApi().getPatientbyId(GlobalObjects.patient.pid)
        medications = PatientApi().getMedicines(GlobalObjects.patient.pid)
        for (med in medications) {
            if (med.medicationId == medicationId) {
                medication = med
            }
        }
    }

    val controller by remember{mutableStateOf(medicationController)}

    val showAddMedicationErrorDialog = remember { mutableStateOf(false) }

    val showValidationErrorDialog = remember { mutableStateOf(false) }

    val showDuplicateMedicationErrorDialog = remember { mutableStateOf(false) }

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

    var startDateState = rememberDatePickerState()

    var endDateState = rememberDatePickerState()

    // TODO: Implement some sort of error, and pass the boolean and the error message to the input fields
    // TODO: Also make the inputs span the width of the screen
    // TODO: Implement validation

    fun validateInputs(
    ): Boolean {
        println("HERE MEDICATION")
        println(medicationViewModel.name.value.isNotBlank())
        println("HERE DOSAGE")
        println(medicationViewModel.startDate.value != null)
        println("HERE TIMES")
        println(medicationViewModel.times)
        println(medicationViewModel.times.value.isNotEmpty())
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


    var searchResults by remember { mutableStateOf<List<String>>(emptyList()) }
    var searchTerm by remember { mutableStateOf(TextFieldValue("")) }
    var selectedSuggestion by remember { mutableStateOf("") }

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
                            CustomTimePicker(medicationViewModel, timeState)
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
                                    .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(5.dp))
                            )
                            // button without container
//                            IconButton(
////                                label = "Add Time",
//                                onClick = { viewModel.addTimePickerState() },
////                                shape = RoundedCornerShape(5.dp),
////                                containerColor = MaterialTheme.colorScheme.primary
////                                colors = ButtonDefaults.buttonColors(
////                                    containerColor = MaterialTheme.colorScheme.primary,
////                                    contentColor = Color.White,
////                                    disabledContainerColor = MaterialTheme.colorScheme.primary,
////                                    disabledContentColor = MaterialTheme.colorScheme.onTertiary
////                                ),
//                                modifier = Modifier.padding(horizontal = 20.dp, vertical = 2.dp)
//                            ) {
//                                Icon(Icons.Default.Add, contentDescription = "Add Time")
//                            }
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
    @Composable
    fun MedicationSearchBar(
        onSearch: () -> Unit,
        suggestions: List<String>
    ) {
        var expanded by remember { mutableStateOf(false) }

        var suggestionClicked by remember { mutableStateOf(false) }

        fun searchWithDelay(term: TextFieldValue) {
            searchJob?.cancel()
            searchJob = scope.launch {
                delay(300)
                onSearch()
//                if (term.text.isNotBlank()) {
//                    onSearch()
//                } else {
//                    searchResults = emptyList()
//                }
            }
        }

        Column {
            OutlinedTextField(
                value = searchTerm,
                onValueChange = { term ->
                    searchTerm = term
                    if (term.text.isNotBlank()) {
                        searchWithDelay(term)
                    } else {
                        searchResults = emptyList()
                    }
                    expanded = (term.text.isNotBlank() && !suggestionClicked) // Expand dropdown only when there's text
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
                                text = suggestion,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
//                                        coroutineScope.launch {
////                                        onSuggestionSelected(suggestion) {
                                        suggestionClicked = true
                                        controller.invoke(
                                            MedicationViewEvent.NameEvent,
                                            suggestion
                                        )
                                        println(medicationViewModel.name.value)

                                        controller.invoke(
                                            MedicationViewEvent.MedicationIdEvent,
                                            suggestion
                                        )
                                        searchTerm =
                                            TextFieldValue(
                                                suggestion,
                                                TextRange(suggestion.length)
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

            // Text input for entering medication manually
//            TextInput(
//                label = "Enter your medication",
//                placeholder = "Medication Name",
//                value = searchTerm,
//                onValueChange = { searchTerm = it }
//            )
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
                    searchResults = MedicationApi().getAllMedicationsbyName(searchTerm.text)
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
                    controller.invoke(MedicationViewEvent.StartDateEvent, Date(startDateState.selectedDateMillis?.let { it + TimeUnit.DAYS.toMillis(1) } ?: System.currentTimeMillis()))

                    // controller.invoke(MedicationViewEvent.StartDateEvent, startDateState.selectedDateMillis?.let { Date(it) } ?: Date(System.currentTimeMillis()))
                }
                CustomDatePicker(endDateState, "End Date") {
                    controller.invoke(MedicationViewEvent.EndDateEvent, Date(endDateState.selectedDateMillis?.let { it + TimeUnit.DAYS.toMillis(1) } ?: System.currentTimeMillis()))

                    // controller.invoke(MedicationViewEvent.EndDateEvent, endDateState.selectedDateMillis?.let { Date(it) } ?: Date(System.currentTimeMillis()))
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


            val times = medicationViewModel.getSelectedTimes()
            var formattedTimes = ""

            if (times.isNotEmpty()) {
                formattedTimes = medicationViewModel.getSelectedFormattedTimes().joinToString(separator = ", ")
            }

            MedicationSummaryCard(
                name = medicationViewModel.name.value,
                dosage = medicationViewModel.amount.value,
                time = formattedTimes,
                dates = "${medicationViewModel.startDate.value} - ${medicationViewModel.endDate.value}",
                specifications = medicationViewModel.notes.value,
            )
            Spacer(modifier = Modifier.height(30.dp))

            println("HI3")
            println(times)

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
                            onNavigateToMedicationList(GlobalObjects.patient.pid)
                        }
                    },
                    enabled = true
                )

                ButtonSecondary(
                    text = "Change",
                    onClick = {
                        if (validateInputs()) {
                            // Check for duplicate medication
                            if (duplicateMedication()) {
                                showDuplicateMedicationErrorDialog.value = true
                            } else {
                                // If not a duplicate, proceed with adding the medication
                                println("MEDICATION HERE")
                                controller.invoke(
                                    MedicationViewEvent.TimeEvent,
                                    medicationViewModel.getSelectedTimes()
                                )
                                println(medicationViewModel.times)
                                controller.invoke(MedicationViewEvent.UpdateEvent, medicationViewModel)

                                if (medicationViewModel.successfulChange.value) {
                                    onNavigateToMedicationList(GlobalObjects.patient.pid)
                                    medicationViewModel.clearAll()
                                } else {
                                    showAddMedicationErrorDialog.value = true
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
