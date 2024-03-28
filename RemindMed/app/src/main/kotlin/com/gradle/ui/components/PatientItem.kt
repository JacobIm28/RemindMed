package com.gradle.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.gradle.constants.NavArguments
import com.gradle.constants.Routes
import com.gradle.models.Patient
import com.gradle.ui.theme.AppTheme

@Composable
fun PatientItem(patient: Patient, onNavigateMedicationList: (String) -> Unit, isValid: Boolean, isAddPatient: Boolean) {
    AppTheme {
        Card(
            modifier = Modifier.padding(6.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            ),

            colors = CardColors(
                containerColor = if (isValid) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                disabledContentColor = MaterialTheme.colorScheme.onTertiaryContainer
            ),
            onClick = {
                if (!isAddPatient) {
                    onNavigateMedicationList(patient.pid)
                }
            }
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Outlined.Person, contentDescription = null, Modifier.size(50.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(patient.name, style = MaterialTheme.typography.headlineMedium.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    ))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(patient.email)
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}