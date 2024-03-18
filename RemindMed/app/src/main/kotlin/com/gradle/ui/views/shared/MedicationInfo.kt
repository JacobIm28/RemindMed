package com.gradle.ui.views.shared

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.gradle.constants.Routes
import com.gradle.ui.theme.AppTheme
import com.gradle.ui.theme.ButtonPrimary
import com.gradle.ui.theme.container_colour
import com.gradle.apiCalls.Patient as PatientApi

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MedicationInfoScreen(navController: NavController) {
    val testMedications = PatientApi().getMedicines(1)
    println(testMedications)
    AppTheme {
        Scaffold (
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navController.popBackStack() },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Add Medication")
                }
            },
        ) { padding ->
            Column (modifier = Modifier.padding(padding)) {
                Text(text = "Medication info screen works")
            }
        }


    }

}