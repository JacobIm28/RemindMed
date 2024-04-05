package com.gradle.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import com.gradle.ui.theme.AppTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePicker(enabled: Boolean = true, state: DatePickerState, date: String, onDateSelected: (String) -> Unit) {
    var displayedDate by remember { mutableStateOf(date) }

    LaunchedEffect(date) {
        displayedDate = date
    }

    var openDialog by remember {
        mutableStateOf(false)
    }

    AppTheme {
        Box(contentAlignment = Alignment.Center) {
            OutlinedButton(
                onClick = { openDialog = true },
                colors = ButtonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.primary,
                    disabledContainerColor = MaterialTheme.colorScheme.tertiary,
                    disabledContentColor = Color.Gray
                ),
                enabled = enabled,
            ) {
                Text(text = displayedDate)
            }
        }
    }

    if (openDialog) {
        MyDatePickerDialog(
            state = state,
            onDateSelected = { displayedDate = it
                onDateSelected(date)
                             },
            onDismiss = { openDialog = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDatePickerDialog(
    state: DatePickerState,
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val selectedDate = state.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""

    AppTheme {
        DatePickerDialog(
            onDismissRequest = { onDismiss() },
            confirmButton = {
                ButtonSecondary(text = "OK", onClick = {
                    onDateSelected(selectedDate)
                    onDismiss()
                }, enabled = true)
            },
            dismissButton = {
                ButtonSecondary(text = "Cancel", onClick = { onDismiss() }, enabled = true)
            }
        ) {
            DatePicker(
                state = state
            )
        }
    }
}

private fun convertMillisToDate(millis: Long): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = millis
    calendar.add(Calendar.DAY_OF_MONTH, 1)

    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return formatter.format(calendar.time)
}