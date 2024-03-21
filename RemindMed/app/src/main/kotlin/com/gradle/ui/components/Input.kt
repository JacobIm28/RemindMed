package com.gradle.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text2.input.TextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.gradle.ui.theme.AppTheme

@Composable
fun TextInput(
    label: String,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean = false,
    errorMessage: String = "Error"
) {
    AppTheme {
        Column(modifier = Modifier.padding(5.dp)) {
            if (isError) {
                Text(errorMessage, color = MaterialTheme.colorScheme.error)
            } else {
                Text(label)
            }
            Spacer(modifier = Modifier.height(5.dp))
            TextField(
                value = value,
                onValueChange = onValueChange,
                // label = { Text(placeholder) },
                maxLines = 1,
                isError = isError,
                trailingIcon = {
                    if (isError) Icon(Icons.Filled.Info, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                }
            )
        }
    }
}

@Composable
fun MultilineTextInput(
    label: String,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    lines: Int,
    isError: Boolean = false,
    errorMessage: String = "Error"
) {
    AppTheme {
        Column(modifier = Modifier.padding(5.dp)) {
            if (isError) {
                Text(errorMessage, color = MaterialTheme.colorScheme.error)
            } else {
                Text(label)
            }
            Spacer(modifier = Modifier.height(5.dp))
            TextField(
                value = value,
                onValueChange = onValueChange,
                label = { Text(placeholder) },
                maxLines = lines,
                isError = isError,
                trailingIcon = {
                    if (isError) Icon(Icons.Filled.Info, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                }
            )
        }
    }
}