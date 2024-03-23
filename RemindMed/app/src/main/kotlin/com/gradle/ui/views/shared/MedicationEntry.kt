package com.gradle.ui.views.shared

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

//import com.gradle.models.Patient

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
import com.gradle.apiCalls.Patient
import com.gradle.constants.NavArguments
import com.gradle.constants.Routes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.zIndex
import com.gradle.constants.GlobalObjects
import com.gradle.ui.components.ButtonSecondary
import com.gradle.ui.components.CustomDatePicker
import com.gradle.ui.components.CustomTimePicker
import com.gradle.ui.components.MedicationInput
import com.gradle.ui.components.MedicationSummaryCard
import com.gradle.ui.components.TextInput
import com.gradle.ui.components.TitleLarge
import com.gradle.ui.theme.AppTheme
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.sql.Date
import java.sql.Time

import java.time.LocalTime
import com.gradle.apiCalls.Medication as MedicationApi
import com.gradle.models.Medication as Medication

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@SuppressLint("RememberReturnType", "UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MedicationEntryScreen(navController: NavController) {

    // TODO: Add all these inputs to the page, then call Samir's function that calls the correct endpoint
    // TODO: Use the reusable Date, TimePicker components
    // TODO: Times should be an array of times, so you have to find a way to be able to select multiple times and add it to a list

    val showAddMedicationErrorDialog = remember { mutableStateOf(false) }

    val showValidationErrorDialog = remember { mutableStateOf(false) }

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
            text = { Text("Please check your inputs. All fields are required and must be valid.") },
            confirmButton = {
                Button(onClick = { showValidationErrorDialog.value = false }) {
                    Text("OK")
                }
            }
        )
    }

    var medicationName by rememberSaveable {
        mutableStateOf("")
    }

    var mid by rememberSaveable {
        mutableStateOf("")
    }
    var amount by rememberSaveable {
        mutableStateOf("")
    }
    var startDate = Date(System.currentTimeMillis())

    var startDateState = rememberDatePickerState()

    var endDateState = rememberDatePickerState()

//    var endState = rememberDateRangePickerState()
////    var endDate by rememberSaveable {
////        mutableStateOf("")
////    }
    var endDate = Date(System.currentTimeMillis())

    var times by rememberSaveable {
        mutableStateOf(mutableListOf<Time>())
    }
    var timeState = rememberTimePickerState()

    var dosage by rememberSaveable {
        mutableStateOf("")
    }

    var notes by rememberSaveable {
        mutableStateOf("")
    }
//    val notes = remember { mutableStateOf(TextFieldValue("")) }

    // TODO: Attach user pid to medication object before sending to backend
    // TODO: Implement search bar, then attach medication id to Medication object

    // TODO: Implement some sort of error, and pass the boolean and the error message to the input fields
    // TODO: Also make the inputs span the width of the screen
    // TODO: Implement validation

    fun validateInputs(
        medicationName: String,
        amount: String,
        times: MutableList<Time>,
        notes: String
    ): Boolean {
        println("HERE MEDICATION")
        println(medicationName.isNotBlank())
        println("HERE DOSAGE")
        println(amount.isNotBlank())
        return medicationName.isNotBlank() && amount.isNotBlank()
    }


    var searchResults by remember { mutableStateOf<List<String>>(emptyList()) }
    var searchTerm by remember { mutableStateOf(TextFieldValue("")) }
    var selectedSuggestion by remember { mutableStateOf("") }

    @Composable
    fun MedicationSearchBar(
        onSearch: () -> Unit,
        onSuggestionSelected: (String) -> Unit,
        suggestions: List<String>
    ) {
        var expanded by remember { mutableStateOf(false) }

        Column {
            OutlinedTextField(
                value = searchTerm,
                onValueChange = { term ->
                    searchTerm = term
                    // Trigger search only if the term is not empty
                    if (term.text.isNotBlank()) {
                        onSearch() // Call onSearch without any arguments
                    } else {
                        // Clear the search results if the search term is empty
                        searchResults = emptyList()
                    }
                    // Expand dropdown only when there's text
                    expanded = (term.text.isNotBlank())
                },
                label = { Text("Search Medication") },
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
                                        onSuggestionSelected(suggestion)
                                        searchTerm = TextFieldValue(suggestion, TextRange(suggestion.length))
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
    }


    AppTheme {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            TitleLarge("Enter Medication")
            Spacer(modifier = Modifier.height(16.dp))


//            TextInput(label = "Enter", placeholder = { Text("Name") }, value = name, onValueChange = { name = it })
            // TextInput(label = "Enter your medication", placeholder = "Medication Name", value = medicationName, onValueChange = { medicationName = it })

            MedicationSearchBar(
                onSearch = {
                    searchResults = MedicationApi().getAllMedicationsbyName(searchTerm.text)
                },
                onSuggestionSelected = { selectedSuggestion ->
                    medicationName = selectedSuggestion
                    println("Selected medication: $selectedSuggestion")
                },
                suggestions = searchResults
            )

//            MedicationInput(
//                onItemSelected = {
//                    medicationName = it
//                }
//            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
//                CustomDatePicker(
//                    onStartDateSelected = { date -> startDate = date },
//                    onEndDateSelected = { date -> endDate = date }
//                )
                CustomDatePicker(startDateState, "Start Date")
                Spacer(modifier = Modifier.width(12.dp))
                CustomDatePicker(endDateState, "End Date")
            }
            Spacer(modifier = Modifier.width(12.dp))
            CustomTimePicker(timeState)

           Spacer(modifier = Modifier.height(16.dp))
//
           OutlinedTextField(
               value = dosage,
               onValueChange = { dosage = it },
               label = { Text("Dosage") },
               modifier = Modifier.fillMaxWidth()
           )
           Spacer(modifier = Modifier.height(16.dp))
//
//            OutlinedTextField(
//                value = time.value.text,
//                onValueChange = { time.value = TextFieldValue(it) },
//                label = { Text("Time") }
//            )
//            Spacer(modifier = Modifier.height(30.dp))
//
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Notes") },
                modifier = Modifier.fillMaxWidth()
            )
//            Spacer(modifier = Modifier.height(30.dp))
//
            Spacer(modifier = Modifier.height(30.dp))

            val formattedHour = if (timeState.hour == 0 || timeState.hour == 12) "12" else String.format("%02d", timeState.hour % 12)
            val paddedMinute = String.format("%02d", timeState.minute)
            val period = if (timeState.hour < 12) "AM" else "PM"

            val startDate = startDateState.selectedDateMillis?.let { Date(it) } ?: Date(System.currentTimeMillis())
            val endDate = endDateState.selectedDateMillis?.let { Date(it) } ?: Date(System.currentTimeMillis())

            MedicationSummaryCard(
                name = medicationName,
                dosage = dosage,
                time = "$formattedHour:$paddedMinute $period",

                dates = "$startDate - $endDate",
                specifications = notes,
            )
            Spacer(modifier = Modifier.height(50.dp))

            val times = mutableListOf(Time(timeState.hour, timeState.minute, 0))

            val medicationResult = MedicationApi().getMedicationbyName(medicationName)
            if (medicationResult != null && medicationResult.has("results")) {
                val resultsArray = medicationResult.getAsJsonArray("results")
                if (resultsArray.size() > 0) {
                    mid = resultsArray[0].asJsonObject["id"].asString
                    println("MEDICATION ID $mid")
                } else {
                    println("Results array is empty")
                }
            } else {
                println("Medication result is null or does not contain 'results' field")
            }
            println("HI3")
            println(startDate)
            println(endDate)
            println(times)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ButtonSecondary(text = "Cancel", onClick = {
                    if (GlobalObjects.type == "doctor")
                        navController.navigate(Routes.PEOPLE_LIST)
                    else
                        navController.navigate(Routes.MEDICATION_LIST + "?" +
                            "${NavArguments.MEDICATION_LIST.PID}=${GlobalObjects.patient.pid}") }, enabled = true)

                ButtonSecondary(text = "Add", onClick = {if (validateInputs(medicationName, dosage, times, notes)) { // need to make validation more robust later on
                    println("HI3")
                    println(startDate)
                    println(endDate)
                    println(times)
                    val medication = Medication(
                        pid = GlobalObjects.patient.pid,
                        medicationId = mid,
                        amount = dosage,
                        startDate = startDate,
                        endDate = endDate,
//                        startDate = Date(0),
//                        endDate = Date(0),
                        name = medicationName,
                        notes = notes,
                        times = times
                    )
//                    println(medication)
                    val success = Patient().addMedication(medication)
                    if (success) {
                        navController.navigate(Routes.MEDICATION_LIST + "?" + "${NavArguments.MEDICATION_LIST.PID}=${GlobalObjects.patient.pid}")
                    } else {
                        showAddMedicationErrorDialog.value = true
                    }
                } else {
                    showValidationErrorDialog.value = true
                } }, enabled = true)
            }
        }
    }
}
