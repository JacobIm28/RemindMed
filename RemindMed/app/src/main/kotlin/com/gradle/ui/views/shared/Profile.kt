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
import com.gradle.controller.DoctorController
import com.gradle.controller.PatientController
import com.gradle.models.Doctor
import com.gradle.models.Patient
import com.gradle.ui.views.DoctorViewModel
import com.gradle.ui.views.PatientViewModel

enum class ProfileViewEvent {
    NameEvent,
    EmailEvent,
    UpdateEvent
}
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("RememberReturnType", "UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ProfileScreen(navController: NavController, doctorViewModel: DoctorViewModel, doctorController: DoctorController) {
    val viewModel by remember{mutableStateOf(doctorViewModel)}
    val controller by remember{mutableStateOf(doctorController)}
    var changesSubmitted by remember{ mutableStateOf(false) }

    AppTheme {
        Box(modifier = Modifier.verticalScroll(rememberScrollState())){
            Column (modifier = androidx.compose.ui.Modifier.padding()) {
                TitleLarge("Profile")
                Spacer(modifier = Modifier.height(16.dp))

                TextInput("Name", "", viewModel.name.value, {controller.invoke(ProfileViewEvent.NameEvent, it)})
                Spacer(modifier = Modifier.height(12.dp))

                TextInput("Email", "", viewModel.email.value, {controller.invoke(ProfileViewEvent.EmailEvent, it)})
                Spacer(modifier = Modifier.height(12.dp))

                Row (modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    ButtonPrimary("Submit", {
                        changesSubmitted = true
                        controller.invoke(ProfileViewEvent.UpdateEvent, "")
                                            }, true)
                }
                Spacer(modifier = Modifier.height(16.dp))

                Row (modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    if (changesSubmitted && viewModel.successfulChange.value) {
                        Text("Success!")
                    } else if (changesSubmitted) {
                        Text("Unfortunately, the changes did not go through")
                        if (viewModel.errorMessage.value.isNotBlank()) {
                            Text(viewModel.errorMessage.value)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("RememberReturnType", "UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ProfileScreen(navController: NavController, patientViewModel: PatientViewModel, patientController: PatientController) {
    val viewModel by remember{mutableStateOf(patientViewModel)}
    val controller by remember{mutableStateOf(patientController)}
    var changesSubmitted by remember{ mutableStateOf(false) }

    AppTheme {
        Box(modifier = Modifier.verticalScroll(rememberScrollState())){
            Column (modifier = androidx.compose.ui.Modifier.padding()) {
                TitleLarge("Profile")
                Spacer(modifier = Modifier.height(16.dp))

                TextInput("Name", "", viewModel.name.value, {controller.invoke(ProfileViewEvent.NameEvent, it)})
                Spacer(modifier = Modifier.height(12.dp))

                TextInput("Email", "", viewModel.email.value, {controller.invoke(ProfileViewEvent.EmailEvent, it)})
                Spacer(modifier = Modifier.height(12.dp))

                Row (modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    ButtonPrimary("Submit", {
                        changesSubmitted = true
                        controller.invoke(ProfileViewEvent.UpdateEvent, "")
                    }, true)
                }
                Spacer(modifier = Modifier.height(16.dp))

                Row (modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    if (changesSubmitted && viewModel.successfulChange.value) {
                        Text("Success!")
                    } else if (changesSubmitted) {
                        Text("Unfortunately, the changes did not go through")
                        if (viewModel.errorMessage.value.isNotBlank()) {
                            Text(viewModel.errorMessage.value)
                        }
                    }
                }
            }
        }
    }
}