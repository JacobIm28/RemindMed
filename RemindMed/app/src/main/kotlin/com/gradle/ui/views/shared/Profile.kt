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
import com.gradle.ui.viewModels.DoctorViewModel
import com.gradle.ui.viewModels.PatientViewModel
import android.os.Handler
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.text.font.FontWeight
import com.gradle.models.LoginModel

enum class ProfileViewEvent {
    NameEvent,
    EmailEvent,
    UpdateEvent,
    DismissEvent,
    LogoutClicked,
    LogoutConfirmed
}
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("RememberReturnType", "UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ProfileScreen(doctorViewModel: DoctorViewModel, doctorController: DoctorController, loginModel: LoginModel) {
    var doctorModel : DoctorViewModel by remember{ mutableStateOf(DoctorViewModel(Doctor())) }
    var doctorController : DoctorController by remember{ mutableStateOf(DoctorController(Doctor(), loginModel)) }
    var viewModel by remember{ mutableStateOf(doctorModel) }
    var controller by remember{ mutableStateOf(doctorController) }
    LaunchedEffect(Unit) {
        val doctor : Doctor = DoctorApi().getDoctor(GlobalObjects.doctor.did)
        doctorModel = DoctorViewModel(doctor)
        doctorController = DoctorController(doctor, loginModel)
        viewModel = doctorModel
        controller = doctorController
    }

    AppTheme {
        Box(modifier = Modifier.verticalScroll(rememberScrollState())){
            Column (modifier = androidx.compose.ui.Modifier.padding()) {
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Profile",
                        modifier = Modifier.padding(top = 13.dp, bottom = 5.dp).weight(1f),
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    IconButton(onClick = {controller.invoke(ProfileViewEvent.LogoutClicked, "")}) {
                        Icon(
                            imageVector = Icons.Outlined.ExitToApp,
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = "Back"
                        )
                    }
                }
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
                } else if (viewModel.changesSubmitted.value && !viewModel.successfulChange.value) {
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

                if (viewModel.logoutClicked.value) {
                    AlertDialog(
                        onDismissRequest = {controller.invoke(ProfileViewEvent.DismissEvent, "")},
                        text = { Text("Are you sure you want to log out?") },
                        confirmButton = {
                            Button(onClick = { controller.invoke(ProfileViewEvent.DismissEvent, "") }) {
                                Text("CANCEL")
                            }
                            Button(onClick = { controller.invoke(ProfileViewEvent.LogoutConfirmed, "") }) {
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
fun ProfileScreen(patientViewModel: PatientViewModel, patientController: PatientController, loginModel: LoginModel) {
  var patientModel : PatientViewModel by remember{ mutableStateOf(PatientViewModel(Patient())) }
  var patientController : PatientController by remember{ mutableStateOf(PatientController(Patient(), loginModel)) }
  var viewModel by remember{ mutableStateOf(patientModel) }
  var controller by remember{ mutableStateOf(patientController) }
  LaunchedEffect(Unit) {
      val patient : Patient = PatientApi().getPatientbyId(GlobalObjects.patient.pid)
      patientModel = PatientViewModel(patient)
      patientController = PatientController(patient, loginModel)
      viewModel = patientModel
      controller = patientController
  }

    AppTheme {
        Box(modifier = Modifier.verticalScroll(rememberScrollState())){
            Column (modifier = androidx.compose.ui.Modifier.padding().fillMaxWidth()) {
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Profile",
                        modifier = Modifier.padding(top = 13.dp, bottom = 5.dp).weight(1f),
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    IconButton(onClick = {controller.invoke(ProfileViewEvent.LogoutClicked, "")}) {
                        Icon(
                            imageVector = Icons.Outlined.ExitToApp,
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = "Back"
                        )
                    }
                }
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
                } else if (viewModel.changesSubmitted.value && !viewModel.successfulChange.value) {
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

                if (viewModel.logoutClicked.value) {
                    AlertDialog(
                        onDismissRequest = {controller.invoke(ProfileViewEvent.DismissEvent, "")},
                        text = { Text("Are you sure you want to log out?") },
                        confirmButton = {
                            Button(onClick = { controller.invoke(ProfileViewEvent.DismissEvent, "") }) {
                                Text("CANCEL")
                            }
                            Button(onClick = { controller.invoke(ProfileViewEvent.LogoutConfirmed, "") }) {
                                Text("OK")
                            }
                        }
                    )
                }
            }
        }
    }
}
