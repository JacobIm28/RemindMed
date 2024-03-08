package com.gradle.views

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@SuppressLint("RememberReturnType", "UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MedicationEntryScreen() {
    val medicationName = remember { mutableStateOf(TextFieldValue("Reserphine")) }
    val dosage = remember { mutableStateOf(TextFieldValue("3 ml per usage")) }
    val time = remember { mutableStateOf(TextFieldValue("9:00 AM")) }
    val specifications = remember { mutableStateOf(TextFieldValue("After Breakfast")) }

    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    // integrate with user name later
                    title = { Text("Enter Medication: ") }
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = medicationName.value.text,
                    onValueChange = { medicationName.value = TextFieldValue(it) },
                    label = { Text("Medication Name") }
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = dosage.value.text,
                    onValueChange = { dosage.value = TextFieldValue(it) },
                    label = { Text("Dosage") }
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = time.value.text,
                    onValueChange = { time.value = TextFieldValue(it) },
                    label = { Text("Time") }
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = specifications.value.text,
                    onValueChange = { specifications.value = TextFieldValue(it) },
                    label = { Text("Specifications") }
                )
                Spacer(modifier = Modifier.height(16.dp))

                MedicationSummaryCard(
                    name = medicationName.value.text,
                    dosage = dosage.value.text,
                    time = time.value.text,
                    specifications = specifications.value.text
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = { /* TODO: Handle cancel action */ }) {
                        Text("Cancel")
                    }
                    Button(onClick = { /* TODO: Handle add action */ }) {
                        Text("Add")
                    }
                }
            }
        }
    }
}

@Composable
fun MedicationSummaryCard(name: String, dosage: String, time: String, specifications: String) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Replace with an actual image
//            Image(
//                painter = painterResource(id = R.drawable.ic_medication_placeholder),
//                contentDescription = "Medication Image"
//            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(name, style = MaterialTheme.typography.h6)
                Text("Dosage: $dosage")
                Text(time)
                Text(specifications)
            }
        }
    }
}
