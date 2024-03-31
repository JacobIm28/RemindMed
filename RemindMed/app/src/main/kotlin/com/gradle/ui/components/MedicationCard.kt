package com.gradle.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun MedicationSummaryCard(name: String, dosage: String, time: String, dates: String, specifications: String) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.LightGray, RoundedCornerShape(15.dp)),


        colors = CardColors(
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.primary,
            disabledContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
            disabledContentColor = MaterialTheme.colorScheme.onTertiaryContainer
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Replace with your actual image resource
//            Image(
//                painter = painterResource(id = R.drawable.ic_medication_placeholder),
//                contentDescription = "Medication Image"
//            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text("Medication: $name", fontWeight = FontWeight.Bold)
                Text("Dosage: $dosage", style = MaterialTheme.typography.bodyMedium)
                Text("Dates: $dates", style = MaterialTheme.typography.bodyMedium)
                Text("Times: $time", style = MaterialTheme.typography.bodyMedium)
                Text("Notes: $specifications", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
