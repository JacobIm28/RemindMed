package com.gradle.ui.theme

import android.service.autofill.OnClickAction
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@Composable
fun TitleLarge(text: String) {
    AppTheme {
        Text(
            text,
            modifier = Modifier.padding(top = 13.dp, bottom = 5.dp),
            style = MaterialTheme.typography.titleLarge.copy(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
fun HeadlineLarge(text: String) {
    AppTheme {
        Text(
            text,
            modifier = Modifier.padding(top = 13.dp, bottom = 5.dp),
            style = MaterialTheme.typography.headlineLarge.copy(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

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
            enabled = enabled
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
            enabled = enabled
        ) {
            Text(text)
        }
    }
}
