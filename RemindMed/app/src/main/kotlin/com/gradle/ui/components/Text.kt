package com.gradle.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gradle.ui.theme.AppTheme

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
