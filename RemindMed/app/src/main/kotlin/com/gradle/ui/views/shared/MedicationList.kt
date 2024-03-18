package com.gradle.ui.views.shared

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.gradle.constants.Routes
import com.gradle.models.Medication
import com.gradle.ui.components.ButtonSecondary
import com.gradle.ui.components.HeadlineLarge
import com.gradle.ui.components.TitleLarge
import com.gradle.ui.theme.*

//@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MedicationListScreen(navController: NavController) {
    // Just some starter medications here for now as a view example
    // --> to do: need to integrate the logic and pull from database
//    val doctorAssignedMedications = listOf(
//        Medication("Reserphine", "3 ml per usage", "9:00 AM", "After Breakfast"),
//    )
    val doctorAssignedMedications = listOf<Medication>()

//    val selfAssignedMedications = listOf(
//        Medication("Vitamin D", "4g twice per day", "9:00 AM + 12:00 AM", "After Breakfast + Lunch"),
//    )
    val selfAssignedMedications = listOf<Medication>()

    AppTheme {
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navController.navigate(Routes.MEDICATION_ENTRY)},
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Medication")
                }
            },
        ) { padding ->
            Column (
                Modifier
                    .padding(padding)
            ) {
                TitleLarge("Medication")

                HeadlineLarge("Doctor Assigned")
                LazyColumn {
                    items(doctorAssignedMedications) { medication ->
                        MedicationItem(medication, navController);
                    }
                }
                Spacer(modifier = (Modifier.height(16.dp)))

                Row (modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    ButtonSecondary(text = "See more", onClick = {}, enabled = true)
                }
                Spacer(modifier = (Modifier.height(16.dp)))

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                    thickness = 2.dp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                HeadlineLarge("Self Assigned")
                LazyColumn {
                    items(selfAssignedMedications) { medication ->
                        MedicationItem(medication, navController)
                    }
                }
                Spacer(modifier = (Modifier.height(16.dp)))
                Row (modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    ButtonSecondary(text = "See More", onClick = {}, enabled = true)
                }
            }
        }
    }
}


// TODO: Move into a components folder
@Composable
fun MedicationItem(medication: Medication, navController: NavController) {
    Card(
        modifier = Modifier.padding(6.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),

        colors = CardColors(
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.primary,
            disabledContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
            disabledContentColor = MaterialTheme.colorScheme.onTertiaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
//            Image (
//                // painter = painterResource(id = R.drawable.ic_medication_placeholder), // Replace with the actual image
//                painter = painterResource(id = 1),
//                contentDescription = medication.name,
//                modifier = Modifier.size(48.dp)
//            )
            Icon(Icons.Outlined.Info, contentDescription = null, Modifier.size(50.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text("test", fontWeight = FontWeight.Bold)
                Text("Dosage: asdasd", style = MaterialTheme.typography.bodyMedium)
                Text("test", style = MaterialTheme.typography.bodyMedium)
                Text("asdassdds", style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { navController.navigate(Routes.MEDICATION_INFO)}) {
                Icon(Icons.Default.ArrowForward, contentDescription = "Go to Details")
            }
        }
    }
}



//@Preview(showBackground = true)
//@Composable
//fun MedicationListScreenPreview() {
//    MedicationListScreen()
//}
//
