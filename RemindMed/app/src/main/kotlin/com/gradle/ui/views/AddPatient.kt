package com.gradle.ui.views

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role.Companion.DropdownList
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.gradle.constants.Routes
import com.gradle.ui.theme.AppTheme
import com.gradle.ui.theme.HeadlineLarge
import com.gradle.ui.theme.TitleLarge

//@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPatientScreen(navController: NavController) {
    var patientName by remember{mutableStateOf("")}
    val birthday = rememberDatePickerState(selectableDates = object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            return utcTimeMillis <= System.currentTimeMillis()
        }
    })
    var height by remember{mutableStateOf("")}
    var weight by remember{mutableStateOf("")}

    val possibleBloodTypes = listOf<String>("O-", "O+", "A-", "A+", "B-", "B+", "AB-", "AB+")
    var bloodType by remember{mutableStateOf(possibleBloodTypes[0])}
    var expanded by remember { mutableStateOf(false) }

    var email by remember{mutableStateOf("")}
    val proxyPatient = Patient("Ben Smith", 32, "Male")
    var showPatient by remember{mutableStateOf(false)}
    var patientExists by remember{mutableStateOf(false)}

    AppTheme {
        Scaffold (
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navController.navigate(Routes.LIST) },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
        ) { padding ->
            Box(modifier = Modifier.verticalScroll(rememberScrollState())){
                Column (modifier = Modifier.padding(padding)) {
                    TitleLarge("Add A New Patient")

                    Spacer(modifier = Modifier.height(16.dp))

                    HeadlineLarge("Email")
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(label = {Text("Email")}, value = email, onValueChange = {email = it}, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp), singleLine = true)
                    /*
                    HeadlineLarge("Patient Name")
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(value = patientName, onValueChange = {patientName = it}, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp), singleLine = true)

                    Spacer(modifier = Modifier.height(12.dp))
                    HeadlineLarge("Birthday")
                    Spacer(modifier = Modifier.height(8.dp))
                    DatePicker(state = birthday)

                    Spacer(modifier = Modifier.height(12.dp))
                    HeadlineLarge("Height")
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(label = {Text("cm")}, value = height, onValueChange = {height = it}, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp), singleLine = true)

                    Spacer(modifier = Modifier.height(12.dp))
                    HeadlineLarge("Weight")
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(label = {Text("kg")}, value = weight, onValueChange = {weight = it}, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp), singleLine = true)

                    Spacer(modifier = Modifier.height(12.dp))
                    HeadlineLarge("Blood Type")
                    Spacer(modifier = Modifier.height(8.dp))
                    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = {expanded = it}) {
                        TextField(
                            value = bloodType,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                            },
                            colors = ExposedDropdownMenuDefaults.textFieldColors(),
                            modifier = Modifier.menuAnchor()
                        )

                        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            possibleBloodTypes.forEach{pbt ->
                                DropdownMenuItem(text = {Text(text = pbt)}, onClick = {
                                    bloodType = pbt
                                    expanded = false
                                })
                            }
                        }
                    }
                    */
                    Spacer(modifier = Modifier.height(16.dp))

                    Row (modifier = Modifier.align(Alignment.CenterHorizontally)) {
                        Button(onClick = {
                            // Where Samir will have to connect FE to BE
                            showPatient = true
                        }) {
                            Text("Search for Patient")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (showPatient && patientExists) {
                        PatientItem(patient = proxyPatient, navController = navController)
                    } else if (showPatient && !patientExists) {
                        HeadlineLarge("Unfortunately this patient does not exist")
                    }

                }
            }
        }


    }

}