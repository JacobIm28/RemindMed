package com.gradle.ui.views.doctor

import TextInput
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gradle.apiCalls.DoctorApi
import com.gradle.constants.GlobalObjects
import com.gradle.controller.AddPatientController
import com.gradle.models.AddPatient
import com.gradle.models.Patient
import com.gradle.ui.components.ButtonPrimary
import com.gradle.ui.components.ButtonSecondary
import com.gradle.ui.components.HeadlineLarge
import com.gradle.ui.components.PatientItem
import com.gradle.ui.components.TitleLarge
import com.gradle.ui.theme.AppTheme
import com.gradle.ui.viewModels.AddPatientViewModel

enum class AddPatientViewEvent {
    EmailEvent,
    SearchPatientEvent,
    AddPatientEvent,
    DialogClose
}

@Composable
fun AddPatientScreen(onNavigateToMedicationList: (String) -> Unit) {
    var patients: MutableList<Patient> by remember { mutableStateOf(mutableListOf<Patient>()) }
    var addPatientModel: AddPatient by remember { mutableStateOf(AddPatient("", patients)) }
    var viewModel: AddPatientViewModel by remember {
        mutableStateOf(
            AddPatientViewModel(
                addPatientModel
            )
        )
    }
    var controller: AddPatientController by remember {
        mutableStateOf(
            AddPatientController(
                addPatientModel
            )
        )
    }

    LaunchedEffect(Unit) {
        patients = DoctorApi().getPatients(GlobalObjects.doctor.did)
        addPatientModel = AddPatient(GlobalObjects.doctor.did, patients)
        viewModel = AddPatientViewModel(addPatientModel)
        controller = AddPatientController(addPatientModel)
    }

    AppTheme {
        Box(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Column(modifier = Modifier.padding()) {
                TitleLarge("Enter New Patient")

                Spacer(modifier = Modifier.height(16.dp))

                HeadlineLarge("Email")
                TextInput(
                    "",
                    "",
                    viewModel.email.value,
                    { controller.invoke(AddPatientViewEvent.EmailEvent, it) },
                    !viewModel.emailIsValid.value,
                    "Not a Valid Email"
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    ButtonSecondary(
                        "Search",
                        { controller.invoke(AddPatientViewEvent.SearchPatientEvent, "") },
                        viewModel.emailIsValid.value
                    )
                }

                if (viewModel.currPatient.value != null && viewModel.patientAlreadyUnderDoctor.value) {
                    PatientItem(
                        patient = viewModel.currPatient.value!!,
                        onNavigateToMedicationList,
                        false,
                        true
                    )
                } else if (viewModel.currPatient.value != null && !viewModel.patientAlreadyUnderDoctor.value) {
                    PatientItem(
                        patient = viewModel.currPatient.value!!,
                        onNavigateToMedicationList,
                        true,
                        true
                    )
                } else {
                    Spacer(modifier = Modifier.height(76.dp))
                }

                Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    ButtonSecondary(
                        "Add Patient",
                        { controller.invoke(AddPatientViewEvent.AddPatientEvent, "") },
                        viewModel.currPatient.value != null && !viewModel.patientAlreadyUnderDoctor.value && viewModel.submitEnabled.value
                    )
                }

                if (viewModel.showDialog.value) {
                    AlertDialog(
                        onDismissRequest = {
                            controller.invoke(
                                AddPatientViewEvent.DialogClose,
                                ""
                            )
                        },
                        text = { Text(viewModel.addPatientDialogMessage.value) },
                        confirmButton = {
                            Button(onClick = {
                                controller.invoke(
                                    AddPatientViewEvent.DialogClose,
                                    ""
                                )
                            }) {
                                Text("OK")
                            }
                        }
                    )
                }
            }
        }
    }
}
