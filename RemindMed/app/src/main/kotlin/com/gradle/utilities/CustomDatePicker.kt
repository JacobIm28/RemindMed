package com.gradle.ui.components

import android.widget.DatePicker
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import com.gradle.ui.theme.AppTheme
import java.text.SimpleDateFormat
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePicker(state: DatePickerState) {
    var date by remember {
        mutableStateOf("Select date")
    }

    var openDialog by remember {
        mutableStateOf(false)
    }

    AppTheme {
        Box(contentAlignment = Alignment.Center) {
            Button(
                onClick = { openDialog = true },
                colors = ButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.primary,
                    disabledContainerColor = MaterialTheme.colorScheme.tertiary,
                    disabledContentColor = MaterialTheme.colorScheme.onTertiary
                ),
                enabled = true
            ) {
                Text(text = date)
            }
        }
    }

    if (openDialog) {
        MyDatePickerDialog(
            state = state,
            onDateSelected = { date = it },
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
                ButtonPrimary(text = "Ok", onClick = {
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
    val formatter = SimpleDateFormat("dd/MM/yyyy")
    return formatter.format(Date(millis))
}