package com.gradle.ui.theme

import android.service.autofill.OnClickAction
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*

@Composable
fun TitleLarge(text: String) {
    AppTheme {
        Text(
            text,
            modifier = Modifier.padding(top = 13.dp, bottom = 5.dp),
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
fun HeadlineLarge(text: String) {
    AppTheme {
        Text(
            text,
            modifier = Modifier.padding(top = 13.dp, bottom = 5.dp),
            style = MaterialTheme.typography.headlineLarge
        )
    }
}

@Composable
fun ButtonPrimary(text: String, onClick: () -> Unit, enabled: Boolean) {
    AppTheme {
        Button(
            onClick,
            colors = ButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
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
            colors = ButtonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.tertiary,
                disabledContentColor = MaterialTheme.colorScheme.onTertiary
            ),
            enabled = enabled
        ) {
            Text(text)
        }
    }
}