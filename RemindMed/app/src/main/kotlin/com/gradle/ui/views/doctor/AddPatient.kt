package com.gradle.ui.views.doctor

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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.gradle.constants.Routes
import com.gradle.models.Patient
import com.gradle.ui.components.ButtonPrimary
import com.gradle.ui.components.HeadlineLarge
import com.gradle.ui.components.TitleLarge
import com.gradle.ui.theme.AppTheme
import com.gradle.apiCalls.Patient as PatientApi
import com.gradle.constants.GlobalObjects as GlobalObjects
import com.gradle.apiCalls.Doctor
import com.gradle.ui.views.shared.PatientItem

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
    val proxyPatient = Patient("-1", "", "")
    var currPatient by remember{mutableStateOf(proxyPatient)}
    var showPatient by remember{mutableStateOf(false)}
    var patientExists by remember{mutableStateOf(false)}
    var successfullyAdded by remember{ mutableStateOf(false) }
    var addPatientRequested by remember{ mutableStateOf(false) }

    AppTheme {
        Scaffold (
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navController.navigate(Routes.PEOPLE_LIST) },
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
                    Spacer(modifier = Modifier.height(16.dp))

                    Row (modifier = Modifier.align(Alignment.CenterHorizontally)) {
                        ButtonPrimary("Search", {
                            showPatient = true
                            currPatient = PatientApi().getPatientbyEmail(email)
                            if(currPatient.pid != "-1") {
                                patientExists = true
                            } else {
                                patientExists = false
                            }
                        }, true)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (showPatient && patientExists) {
                        PatientItem(patient = currPatient, navController = navController)
                        Spacer(modifier = Modifier.height(12.dp))

                        Row (modifier = Modifier.align(Alignment.CenterHorizontally)) {
                            ButtonPrimary("Add Patient", {
                                // need to use the doctor add patient here
                                successfullyAdded = Doctor().addPatient(GlobalObjects.doctor.did, currPatient.pid)
                                addPatientRequested = true
                            }, true)
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        if (successfullyAdded && addPatientRequested) {
                            Text("Success!")
                        } else if (addPatientRequested) {
                            Text("Unfortunately could not add patient")
                        }
                    } else if (showPatient) {
                        HeadlineLarge("Unfortunately this patient does not exist")
                    }

                }
            }
        }


    }

}