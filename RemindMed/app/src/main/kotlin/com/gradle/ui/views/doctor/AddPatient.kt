package com.gradle.ui.views.doctor

import TextInput
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
import androidx.compose.runtime.LaunchedEffect
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
import com.gradle.controller.AddPatientController
import com.gradle.models.AddPatient
import com.gradle.ui.components.PatientItem

enum class AddPatientViewEvent{
    EmailEvent,
    SearchPatientEvent,
    AddPatientEvent,
    DialogClose
}

@Composable
fun AddPatientScreen(onNavigateToMedicationList: (String) -> Unit) {
    var patients: MutableList<Patient> by remember {mutableStateOf(mutableListOf<Patient>())}
    var addPatientModel : AddPatient by remember{ mutableStateOf(AddPatient("", patients)) }
    var addPatientViewModel : AddPatientViewModel by remember{mutableStateOf(AddPatientViewModel(addPatientModel))}
    var addPatientController : AddPatientController by remember{mutableStateOf(AddPatientController(addPatientModel))}
    var viewModel by remember{ mutableStateOf(addPatientViewModel) }
    var controller by remember{ mutableStateOf(addPatientController) }
    LaunchedEffect(Unit) {
        patients = Doctor().getPatients(GlobalObjects.doctor.did)
        addPatientModel = AddPatient(GlobalObjects.doctor.did, patients)
        addPatientViewModel = AddPatientViewModel(addPatientModel)
        addPatientController = AddPatientController(addPatientModel)
        viewModel = addPatientViewModel
        controller = addPatientController
    }

    AppTheme {
            Box(modifier = Modifier.verticalScroll(rememberScrollState())){
                Column (modifier = androidx.compose.ui.Modifier.padding()) {
                    TitleLarge("Add A New Patient")

                    Spacer(modifier = Modifier.height(16.dp))

                    HeadlineLarge("Email")
                    TextInput("", "", viewModel.email.value, {controller.invoke(AddPatientViewEvent.EmailEvent, it)}, !viewModel.emailIsValid.value, "Not a valid email")
                    Spacer(modifier = Modifier.height(16.dp))

                    Row (modifier = Modifier.align(Alignment.CenterHorizontally)) {
                        ButtonPrimary("Search", {controller.invoke(AddPatientViewEvent.SearchPatientEvent, "")}, viewModel.emailIsValid.value)
                    }

                    if (viewModel.currPatient.value != null && viewModel.patientAlreadyUnderDoctor.value) {
                        // not a valid patient to add
                        PatientItem(patient = viewModel.currPatient.value!!, onNavigateToMedicationList, false, true)
                    } else if (viewModel.currPatient.value != null && !viewModel.patientAlreadyUnderDoctor.value) {
                        // valid patient to add
                        PatientItem(patient = viewModel.currPatient.value!!, onNavigateToMedicationList , true, true)
                    } else {
                        Spacer(modifier = Modifier.height(76.dp))
                    }

                    Row (modifier = Modifier.align(Alignment.CenterHorizontally)) {
                        ButtonPrimary("Add Patient", {controller.invoke(AddPatientViewEvent.AddPatientEvent, "")}, viewModel.currPatient.value != null && !viewModel.patientAlreadyUnderDoctor.value)
                    }

                    if (viewModel.showDialog.value) {
                        AlertDialog(
                            onDismissRequest = { controller.invoke(AddPatientViewEvent.DialogClose, "") },
                            text = { Text(viewModel.addPatientDialogMessage.value) },
                            confirmButton = {
                                Button(onClick = { controller.invoke(AddPatientViewEvent.DialogClose, "") }) {
                                    Text("OK")
                                }
                            }
                        )
                    }
                }
            }
    }
}
