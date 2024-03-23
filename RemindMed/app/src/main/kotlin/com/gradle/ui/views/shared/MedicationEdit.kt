//package com.gradle.ui.views.shared
//
//import android.annotation.SuppressLint
//import android.os.Build
//import androidx.annotation.RequiresApi
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material3.*
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Add
//import androidx.compose.material.icons.filled.ArrowForward
//import androidx.compose.material.icons.filled.Delete
//import androidx.compose.material.icons.filled.Edit
//import androidx.compose.material.icons.outlined.Info
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavController
//import androidx.navigation.NavDeepLinkBuilder
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//
//import com.gradle.constants.GlobalObjects
//import com.gradle.apiCalls.Patient as PatientApi
//import com.gradle.constants.NavArguments
//import com.gradle.constants.Routes
//import com.gradle.models.LoginModel
//import com.gradle.models.Medication
//import com.gradle.ui.components.ButtonSecondary
//import com.gradle.ui.components.HeadlineLarge
//import com.gradle.ui.components.TitleLarge
//import com.gradle.ui.theme.*
//import com.gradle.utilities.toFormattedDateString
//import java.sql.Date
//import java.sql.Time
//import java.time.format.DateTimeFormatter
//
////@OptIn(ExperimentalMaterial3Api::class)
//@RequiresApi(Build.VERSION_CODES.O)
//@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
//@Composable
//fun MedicationListScreen(navController: NavController) {
//    var medications: List<Medication> = List(0) { Medication("-1", "", "", Date(0), Date(0), "", "", mutableListOf()) }
//    if(GlobalObjects.type == "patient") {
//        medications = PatientApi().getMedicines(GlobalObjects.patient.pid)
//    } else {
//        //selected patient??
//    }
//
//    val selfAssignedMedicationsState = remember { mutableStateOf(medications) } // need to differentiate it as self assigned
//
//    AppTheme {
//        Scaffold(
//            floatingActionButton = {
//                FloatingActionButton(
//                    onClick = { navController.navigate(Routes.MEDICATION_ENTRY)},
//                    containerColor = MaterialTheme.colorScheme.primary
//                ) {
//                    Icon(Icons.Default.Add, contentDescription = "Add Medication")
//                }
//            },
//        ) { padding ->
//            Column (
//                Modifier
//                    .padding(padding)
//            ) {
//
//                TitleLarge("${GlobalObjects.patient?.name?.substringBefore(" ")}'s Medication")
//
//                HeadlineLarge("Medications")
//                LazyColumn {
//                    items(medications) { medication ->
//                        MedicationItem(
//                            medication = medication,
//                            navController = navController,
//                            onRemove = {
//                                selfAssignedMedicationsState.value =
//                                    selfAssignedMedicationsState.value.toMutableList().apply {
//                                        remove(medication)
//                                    }
//                            },
//                            onClick = {
//                                navController.navigate(
//                                    Routes.MEDICATION_INFO + "?${NavArguments.MEDICATION_INFO.MEDICATION_NAME}=${medication.name}&" +
//                                            "${NavArguments.MEDICATION_INFO.START_DATE}=${medication.startDate}&" +
//                                            "${NavArguments.MEDICATION_INFO.END_DATE}=${medication.endDate}&" +
//                                            "${NavArguments.MEDICATION_INFO.DOSAGE}=${medication.amount}"
//                                )
//                            }
//                        )
//                    }
//                }
//
//                Spacer(modifier = (Modifier.height(16.dp)))
//                Row (modifier = Modifier.align(Alignment.CenterHorizontally)) {
//                    ButtonSecondary(text = "See More", onClick = {}, enabled = true)
//                }
//            }
//        }
//    }
//}
//
//
//@RequiresApi(Build.VERSION_CODES.O)
//@Composable
//fun MedicationItem(
//    medication: Medication,
//    navController: NavController,
//    onRemove: () -> Unit,
//    onClick: () -> Unit // Click listener for the entire item
//) {
//    var showDialog by remember { mutableStateOf(false) }
//
//    Card(
//        modifier = Modifier
//            .padding(6.dp)
//            .clickable(onClick = onClick), // Apply click listener to the entire item
//        elevation = CardDefaults.cardElevation(
//            defaultElevation = 4.dp
//        ),
//        colors = CardColors(
//            containerColor = MaterialTheme.colorScheme.tertiary,
//            contentColor = MaterialTheme.colorScheme.primary,
//            disabledContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
//            disabledContentColor = MaterialTheme.colorScheme.onTertiaryContainer
//        )
//    ) {
//        Row(
//            modifier = Modifier
//                .padding(16.dp)
//                .fillMaxWidth(),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            val formatter = DateTimeFormatter.ofPattern("MMMM dd")
//            val formattedStartDate = medication.startDate.toFormattedDateString().format(formatter)
//            val formattedEndDate = medication.endDate.toFormattedDateString().format(formatter)
//
//            println(formattedStartDate)
//            println(formattedEndDate)
//            Icon(Icons.Outlined.Info, contentDescription = null, Modifier.size(50.dp))
//            Spacer(modifier = Modifier.width(8.dp))
//            Column {
//                Text(medication.name, fontWeight = FontWeight.Bold)
//                Text(medication.amount, style = MaterialTheme.typography.bodyMedium)
//                Text("$formattedStartDate - $formattedEndDate", style = MaterialTheme.typography.bodyMedium)
//                Text("${medication.times}", style = MaterialTheme.typography.bodyMedium)
//            }
//            Spacer(modifier = Modifier.weight(1f))
//
//            Column {
//                IconButton(onClick = { navController.navigate(Routes.MEDICATION_EDIT) }) {
//                    Icon(Icons.Filled.Edit, contentDescription = "Edit")
//                }
//                // Remove icon
//                IconButton(onClick = { showDialog = true }) { // Set showDialog to true when remove icon clicked
//                    Icon(Icons.Filled.Delete, contentDescription = "Delete")
//                }
//            }
//        }
//    }
//
//    if (showDialog) {
//        AlertDialog(
//            onDismissRequest = { showDialog = false },
//            title = { Text("Confirm Delete") },
//            text = { Text("Are you sure you want to delete this medication?") },
//            confirmButton = {
//                Button(
//                    onClick = {
//                        onRemove()
//                        showDialog = false
//                    }
//                ) {
//                    Text("Confirm")
//                }
//            },
//            dismissButton = {
//                Button(
//                    onClick = { showDialog = false }
//                ) {
//                    Text("Cancel")
//                }
//            }
//        )
//    }
//}


//@Preview(showBackground = true)
//@Composable
//fun MedicationListScreenPreview() {
//    MedicationListScreen()
//}
//

//package com.gradle.ui.views
//
//import android.annotation.SuppressLint
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.*
//
//import androidx.compose.material3.*
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.input.TextFieldValue
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavController
//import com.gradle.constants.Routes
//import com.gradle.ui.theme.AppTheme
//import com.gradle.ui.components.ButtonSecondary
//import com.gradle.ui.components.TitleLarge
//
//@OptIn(ExperimentalMaterial3Api::class)
//@SuppressLint("RememberReturnType", "UnusedMaterialScaffoldPaddingParameter")
//@Composable
//fun UserMedicationEditScreen(navController: NavController) {
//    val medicationName = remember { mutableStateOf(TextFieldValue("Reserphine")) }
//    val dosage = remember { mutableStateOf(TextFieldValue("3 ml per usage")) }
//    val time = remember { mutableStateOf(TextFieldValue("9:00 AM")) }
//    val specifications = remember { mutableStateOf(TextFieldValue("After Breakfast")) }
//
//    AppTheme {
//        Column() {
//            TitleLarge("Edit Medication")
//            Spacer(modifier = Modifier.height(16.dp))
//
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
//
//            MedicationSummaryCard(
//                name = medicationName.value.text,
//                dosage = dosage.value.text,
//                time = time.value.text,
//                specifications = specifications.value.text
//            )
//
//            Spacer(modifier = Modifier.height(50.dp))
//
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceEvenly
//            ) {
//                ButtonSecondary(text = "Cancel", onClick = { navController.navigate(Routes.MEDICATION_LIST) }, enabled = true)
//
//                ButtonSecondary(text = "Change", onClick = { /* Handle add action */ }, enabled = true)
//            }
//        }
//
//    }
//}
