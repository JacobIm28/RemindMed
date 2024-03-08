package com.gradle.ui.views

import android.annotation.SuppressLint
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

//@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AddPatientScreen(navController: NavController) {
    AppTheme {
        Scaffold (
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navController.navigate(Routes.LIST) },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
        ) { padding ->
            Column (modifier = Modifier.padding(padding)) {
                Text(text = "Add Patient screen works")
            }
        }


    }

}