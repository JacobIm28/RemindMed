package com.gradle.ui.views

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.gradle.constants.Routes
import com.gradle.ui.theme.*

var currName = ""

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun DoctorViewMedicationListScreen(navController: NavController) {
    // Just some starter medications here for now as a view example
    // --> to do: need to integrate the logic and pull from database
    val doctorAssignedMedications = listOf(
        Medication("Reserphine", "3 ml per usage", "9:00 AM", "After Breakfast"),
    )

    val selfAssignedMedications = listOf(
        Medication("Vitamin D", "4g twice per day", "9:00 AM + 12:00 AM", "After Breakfast + Lunch"),
    )

    AppTheme {
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navController.navigate(Routes.LIST)},
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Add Medication")
                }
            },
        ) { padding ->
            Column (
                Modifier
                    .padding(padding)
            ) {
                TitleLarge("$currName's Medication")

                HeadlineLarge("Doctor Assigned")
                LazyColumn {
                    items(doctorAssignedMedications) { medication ->
                        MedicationItem(medication, navController)
                    }
                }
                Row (modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    ButtonSecondary(text = "See more", onClick = {}, enabled = true)
                }

                HeadlineLarge("Self Assigned")
                LazyColumn {
                    items(selfAssignedMedications) { medication ->
                        MedicationItem(medication, navController)
                    }
                }
                Row (modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    ButtonSecondary(text = "See more", onClick = {}, enabled = true)
                }
            }
        }
    }
}
