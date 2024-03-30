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
import androidx.compose.material.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.runtime.LaunchedEffect
import com.gradle.constants.GlobalObjects
import com.gradle.controller.DoctorController
import com.gradle.controller.PatientController
import com.gradle.models.Doctor
import com.gradle.models.Patient
import com.gradle.ui.views.DoctorViewModel
import com.gradle.ui.views.PatientViewModel
import android.os.Handler
import androidx.compose.foundation.layout.fillMaxWidth

enum class ProfileViewEvent {
    NameEvent,
    EmailEvent,
    UpdateEvent,
    DismissEvent
}
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("RememberReturnType", "UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ProfileScreen(doctorViewModel: DoctorViewModel, doctorController: DoctorController) {
    var doctorModel : DoctorViewModel by remember{ mutableStateOf(DoctorViewModel(Doctor())) }
    var doctorController : DoctorController by remember{ mutableStateOf(DoctorController(Doctor())) }
    var viewModel by remember{ mutableStateOf(doctorModel) }
    var controller by remember{ mutableStateOf(doctorController) }
    LaunchedEffect(Unit) {
        val doctor : Doctor = DoctorApi().getDoctor(GlobalObjects.doctor.did)
        doctorModel = DoctorViewModel(doctor)
        doctorController = DoctorController(doctor)
        viewModel = doctorModel
        controller = doctorController
    }

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
//                        viewModel.changesSubmitted.value = true
                        controller.invoke(ProfileViewEvent.UpdateEvent, "")
                                            }, viewModel.submitEnabled.value)
                }
                Spacer(modifier = Modifier.height(16.dp))

                if (viewModel.changesSubmitted.value && viewModel.successfulChange.value) {
                    AlertDialog(
                        onDismissRequest = { controller.invoke(ProfileViewEvent.DismissEvent, "")},
                        text = { Text("Success!")},
                        confirmButton = {
                            Button(onClick = { controller.invoke(ProfileViewEvent.DismissEvent, "") }) {
                                Text("OK")
                            }
                        }
                    )

                    Handler().postDelayed({
                        controller.invoke(ProfileViewEvent.DismissEvent, "")
                    }, 5000)
                } else if (viewModel.changesSubmitted.value && viewModel.errorMessage.value.isNotBlank()) {
                    AlertDialog(
                        onDismissRequest = {controller.invoke(ProfileViewEvent.DismissEvent, "")},
                        text = { Text("Unfortunately, the changes did not go through\n" + viewModel.errorMessage.value)},
                        confirmButton = {
                            Button(onClick = { controller.invoke(ProfileViewEvent.DismissEvent, "") }) {
                                Text("OK")
                            }
                        }
                    )
                } else if (viewModel.changesSubmitted.value) {
                    AlertDialog(
                        onDismissRequest = {controller.invoke(ProfileViewEvent.DismissEvent, "")},
                        text = { Text("Unfortunately, the changes did not go through") },
                        confirmButton = {
                            Button(onClick = { controller.invoke(ProfileViewEvent.DismissEvent, "") }) {
                                Text("OK")
                            }
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("RememberReturnType", "UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ProfileScreen(patientViewModel: PatientViewModel, patientController: PatientController) {
  var patientModel : PatientViewModel by remember{ mutableStateOf(PatientViewModel(Patient())) }
  var patientController : PatientController by remember{ mutableStateOf(PatientController(Patient())) }
  var viewModel by remember{ mutableStateOf(patientModel) }
  var controller by remember{ mutableStateOf(patientController) }
  LaunchedEffect(Unit) {
      val patient : Patient = PatientApi().getPatientbyId(GlobalObjects.patient.pid)
      patientModel = PatientViewModel(patient)
      patientController = PatientController(patient)
      viewModel = patientModel
      controller = patientController
  }

    AppTheme {
        Box(modifier = Modifier.verticalScroll(rememberScrollState())){
            Column (modifier = androidx.compose.ui.Modifier.padding().fillMaxWidth()) {
                TitleLarge("Profile")
                Spacer(modifier = Modifier.height(16.dp))

                TextInput("Name", "", viewModel.name.value, {controller.invoke(ProfileViewEvent.NameEvent, it)})
                Spacer(modifier = Modifier.height(12.dp))

                TextInput("Email", "", viewModel.email.value, {controller.invoke(ProfileViewEvent.EmailEvent, it)})
                Spacer(modifier = Modifier.height(12.dp))

                Row (modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    ButtonPrimary("Submit", {
                        viewModel.changesSubmitted.value = true
                        controller.invoke(ProfileViewEvent.UpdateEvent, "")
                    }, viewModel.submitEnabled.value)
                }
                Spacer(modifier = Modifier.height(16.dp))

                if (viewModel.changesSubmitted.value && viewModel.successfulChange.value) {
                    AlertDialog(
                        onDismissRequest = { controller.invoke(ProfileViewEvent.DismissEvent, "")},
                        text = { Text("Success!")},
                        confirmButton = {
                            Button(onClick = { controller.invoke(ProfileViewEvent.DismissEvent, "") }) {
                                Text("OK")
                            }
                        }
                    )
                    Handler().postDelayed({
                        controller.invoke(ProfileViewEvent.DismissEvent, "")
                    }, 5000)
                } else if (viewModel.changesSubmitted.value && viewModel.errorMessage.value.isNotBlank()) {
                    AlertDialog(
                        onDismissRequest = {controller.invoke(ProfileViewEvent.DismissEvent, "")},
                        text = { Text("Unfortunately, the changes did not go through\n" + viewModel.errorMessage.value)},
                        confirmButton = {
                            Button(onClick = { controller.invoke(ProfileViewEvent.DismissEvent, "")}) {
                                Text("OK")
                            }
                        }
                    )
                } else if (viewModel.changesSubmitted.value) {
                    AlertDialog(
                        onDismissRequest = {controller.invoke(ProfileViewEvent.DismissEvent, "")},
                        text = { Text("Unfortunately, the changes did not go through") },
                        confirmButton = {
                            Button(onClick = { controller.invoke(ProfileViewEvent.DismissEvent, "") }) {
                                Text("OK")
                            }
                        }
                    )
                }
            }
        }
    }
}
