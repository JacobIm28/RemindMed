package com.gradle.ui.views.shared

import TextInput
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.gradle.apiCalls.Patient as PatientApi
import com.gradle.apiCalls.Doctor as DoctorApi
import com.gradle.ui.components.ButtonPrimary
import com.gradle.ui.components.TitleLarge
import com.gradle.ui.theme.AppTheme
import com.gradle.constants.GlobalObjects
import com.gradle.models.Doctor
import com.gradle.models.Patient

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("RememberReturnType", "UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ProfileScreen(navController: NavController) {

    var emailChanged by remember{ mutableStateOf(false) }
    var nameChanged by remember{ mutableStateOf(false) }

    var ogName: String = ""
    var ogEmail: String = ""
    var name by remember{ mutableStateOf("") }
    var email by remember{ mutableStateOf("") }
    if (GlobalObjects.type == "patient") {
        ogName = GlobalObjects.patient.name
        ogEmail = GlobalObjects.patient.email
        name = GlobalObjects.patient.name
        email = GlobalObjects.patient.email
    } else {
        ogName = GlobalObjects.doctor.name
        ogEmail = GlobalObjects.doctor.email
        name = GlobalObjects.doctor.name
        email = GlobalObjects.doctor.email
    }

    var errorMessage by remember{mutableStateOf("")}

    var changesSubmitted by remember{mutableStateOf(false)}
    var isSuccess by remember{mutableStateOf(false)}
    var isError by remember{mutableStateOf(false)}

    AppTheme {
        Box(modifier = Modifier.verticalScroll(rememberScrollState())){
            Column (modifier = androidx.compose.ui.Modifier.padding()) {
                TitleLarge("Profile")
                Spacer(modifier = Modifier.height(16.dp))

                TextInput("Name", "", name, {
                    name = it
                    nameChanged = (name != ogName && name.isNotBlank())
                }, isError = isError, errorMessage = errorMessage)
                Spacer(modifier = Modifier.height(12.dp))

                TextInput("Email", "", email, {
                    email = it
                    emailChanged = (email != ogEmail && email.isNotBlank() && "@" in email)
                    // TODO: Change email in auth02
                })
                Spacer(modifier = Modifier.height(12.dp))

                Row (modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    ButtonPrimary("Submit", {
                        if (GlobalObjects.type == "doctor") {
                            val newDoc: Doctor = Doctor(GlobalObjects.doctor.did, name, email)
                            try {
                                isSuccess = DoctorApi().updateDoctor(newDoc)
                            } catch (e: Exception) {
                                errorMessage = e.message.toString()
                                isError = true
                            }
                        } else {
                            val newPat: Patient = Patient(GlobalObjects.patient.pid, name, email)
                            try {
                                isSuccess = PatientApi().updatePatient(newPat)
                            } catch (e: Exception) {
                                errorMessage = e.message.toString()
                                isError = true
                            }
                        }
                        changesSubmitted = true
                    }, (emailChanged || nameChanged))
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row (modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    if (changesSubmitted && isSuccess) {
                        Text("Success!")
                    } else if (changesSubmitted) {
                        Text("Unfortunately, the changes did not go through")
                        if (errorMessage.isNotBlank()) {
                            Text(errorMessage)
                        }
                    }
                }
            }
        }
    }
}
