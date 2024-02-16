package com.gradle.views

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class Medication(val name: String, val dosage: String, val time: String, val instructions: String)
// have to integrate with the medication model

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MedicationListScreen() {
    // Just some starter medications here for now as a view example
    // --> to do: need to integrate the logic and pull from database
    val doctorAssignedMedications = listOf(
        Medication("Reserphine", "3 ml per usage", "9:00 AM", "After Breakfast"),
    )

    val selfAssignedMedications = listOf(
        Medication("Vitamin D", "4g twice per day", "9:00 AM + 12:00 AM", "After Breakfast + Lunch"),
    )

    Scaffold(
        topBar = { TopAppBar(title = { Text("Medications") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { /* TODO: Handle the add medication click */ }) {
                Icon(Icons.Default.Add, contentDescription = "Add Medication")
            }
        },
        bottomBar = { /* TODO: Add bottom navigation bar if needed */ },
        content = {
            Column {
                Text("Doctor Assigned", fontWeight = FontWeight.Bold)
                LazyColumn {
                    items(doctorAssignedMedications) { medication ->
                        MedicationItem(medication)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text("Self Assigned", fontWeight = FontWeight.Bold)
                LazyColumn {
                    items(selfAssignedMedications) { medication ->
                        MedicationItem(medication)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { /* TODO: Handle the see more click */ }) {
                    Text("See more")
                }
            }
        }
    )
}

@Composable
fun MedicationItem(medication: Medication) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
//            Image (
//                // painter = painterResource(id = R.drawable.ic_medication_placeholder), // Replace with the actual image
//                contentDescription = medication.name,
//                modifier = Modifier.size(48.dp)
//            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(medication.name, fontWeight = FontWeight.Bold)
                Text("Dosage: ${medication.dosage}")
                Text(medication.time)
                Text(medication.instructions)
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { /* TODO: Handle navigate to medication details */ }) {
                Icon(Icons.Default.ArrowForward, contentDescription = "Go to details")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MedicationListScreenPreview() {
    MedicationListScreen()
}

