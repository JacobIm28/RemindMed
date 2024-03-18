package com.gradle.ui.views.shared

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState

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
import com.gradle.constants.Routes
import com.gradle.ui.components.ButtonSecondary
import com.gradle.ui.components.CustomDatePicker
import com.gradle.ui.components.CustomTimePicker
import com.gradle.ui.components.MedicationSummaryCard
import com.gradle.ui.components.TextInput
import com.gradle.ui.components.TitleLarge
import com.gradle.ui.theme.AppTheme
import java.sql.Date
import java.sql.Time


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@SuppressLint("RememberReturnType", "UnusedMaterialScaffoldPaddingParameter")
@Composable
fun UserMedicationEntryScreen(navController: NavController) {

    // TODO: Add all these inputs to the page, then call Samir's function that calls the correct endpoint
    // TODO: Use the reusable Date, TimePicker components
    // TODO: Times should be an array of times, so you have to find a way to be able to select multiple times and add it to a list
    var medicationName by rememberSaveable {
        mutableStateOf("")
    }
    var amount by rememberSaveable {
        mutableStateOf("")
    }
    var startDate by rememberSaveable {
        mutableStateOf("")
    }
//    var endDate by rememberSaveable {
//        mutableStateOf("")
//    }
    val endDate = rememberDatePickerState()

    var times by rememberSaveable {
        mutableStateOf(mutableListOf<Time>())
    }
    var time = rememberTimePickerState()

    var notes by rememberSaveable {
        mutableStateOf("")
    }

    // TODO: Attach user pid to medication object before sending to backend
    // TODO: Implement search bar, then attach medication id to Medication object

    // TODO: Implement some sort of error, and pass the boolean and the error message to the input fields
    // TODO: Also make the inputs span the width of the screen
    // TODO: Implement validation

    AppTheme {
        Column() {
            TitleLarge("Enter Medication")
            Spacer(modifier = Modifier.height(16.dp))

//            TextInput(label = "Enter", placeholder = { Text("Name") }, value = name, onValueChange = { name = it })
            TextInput(label = "Enter your medication", placeholder = "Medication Name", value = medicationName, onValueChange = { medicationName = it })

            CustomDatePicker(endDate)
            CustomTimePicker(time)


//            OutlinedTextField(
//                value = medicationName.value.text,
//                onValueChange = { medicationName.value = TextFieldValue(it) },
//                label = { Text("Medication Name") }
//            )
//            Spacer(modifier = Modifier.height(30.dp))
//
//            OutlinedTextField(
//                value = dosage.value.text,
//                onValueChange = { dosage.value = TextFieldValue(it) },
//                label = { Text("Dosage") }
//            )
//            Spacer(modifier = Modifier.height(30.dp))
//
//            OutlinedTextField(
//                value = time.value.text,
//                onValueChange = { time.value = TextFieldValue(it) },
//                label = { Text("Time") }
//            )
//            Spacer(modifier = Modifier.height(30.dp))
//
//            OutlinedTextField(
//                value = specifications.value.text,
//                onValueChange = { specifications.value = TextFieldValue(it) },
//                label = { Text("Specifications") }
//            )
//            Spacer(modifier = Modifier.height(30.dp))
//
//            Spacer(modifier = Modifier.height(30.dp))

//            MedicationSummaryCard(
//                name = medicationName.value.text,
//                dosage = dosage.value.text,
//                time = time.value.text,
//                specifications = specifications.value.text
//            )

//            Spacer(modifier = Modifier.height(50.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ButtonSecondary(text = "Cancel", onClick = { navController.navigate(Routes.MEDICATION_LIST) }, enabled = true)

                ButtonSecondary(text = "Add", onClick = {  }, enabled = true)
            }
        }
    }
}
