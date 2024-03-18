package com.gradle.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.gradle.ui.theme.AppTheme

@Composable
fun ButtonPrimary(text: String, onClick: () -> Unit, enabled: Boolean) {
    AppTheme {
        Button(
            onClick,
            colors = ButtonColors(
                // primary: text
                // secondary: button
                // container: medication cards
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = Color.White,
                disabledContainerColor = MaterialTheme.colorScheme.tertiary,
                disabledContentColor = MaterialTheme.colorScheme.onTertiary
            ),
            enabled = enabled,
            modifier = Modifier.padding(5.dp)
        ) {
            Text(text)
        }
    }
}

@Composable
fun ButtonSecondary(text: String, onClick: () -> Unit, enabled: Boolean) {
    AppTheme {
        FilledTonalButton(
            onClick,
            shape = MaterialTheme.shapes.medium,
            colors = ButtonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = Color.White,
                disabledContainerColor = MaterialTheme.colorScheme.tertiary,
                disabledContentColor = MaterialTheme.colorScheme.onTertiary
            ),
            enabled = enabled,
            modifier = Modifier.padding(5.dp)
        ) {
            Text(text)
        }
    }
}