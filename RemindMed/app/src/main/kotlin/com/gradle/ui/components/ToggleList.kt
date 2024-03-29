package com.gradle.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.gradle.ui.theme.AppTheme

@Composable
fun ToggleList(header: String, content: String) {
    val isExpanded = remember { mutableStateOf(false) }

    AppTheme {
        Column {
            Row(
                modifier = Modifier
                    .clickable { isExpanded.value = !isExpanded.value }
            ) {
                if (isExpanded.value) {
                    HeadLineMedium(text = "▼ $header")
                } else {
                    HeadLineMedium(text = "▶ $header")
                }
            }

            if (isExpanded.value) {
                Text(text = content)
            }
        }

    }
}