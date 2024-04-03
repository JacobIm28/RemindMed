package com.gradle.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.gradle.ui.theme.AppTheme
import com.gradle.ui.viewModels.MedicationViewModel
import java.sql.Time

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTimePicker(medicationViewModel: MedicationViewModel, state: TimePickerState) {
    var time by remember {
        mutableStateOf("Select Time")
    }

    LaunchedEffect(state.hour, state.minute) {
        val formattedHour = if (state.hour == 0 || state.hour == 12) "12" else String.format("%02d", state.hour % 12)
        val paddedMinute = String.format("%02d", state.minute)
        val period = if (state.hour < 12) "AM" else "PM"
        time = "$formattedHour:$paddedMinute $period"
    }

    var openDialog by remember {
        mutableStateOf(false)
    }

    AppTheme {
        Box(contentAlignment = Alignment.Center) {
            OutlinedButton(
                onClick = {
                    openDialog = true
                },
                colors = ButtonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.primary,
                    disabledContainerColor = MaterialTheme.colorScheme.tertiary,
                    disabledContentColor = MaterialTheme.colorScheme.onTertiary
                ),
                enabled = true
            ) {
                Text(text = time)
            }
        }
    }

    if (openDialog) {
        MyTimePickerDialog(
            medicationViewModel = medicationViewModel,
            state = state,
            onDismiss = { openDialog = false },
            onConfirm = { time = it }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTimePickerDialog(
    medicationViewModel: MedicationViewModel,
    state: TimePickerState,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var dialogState by remember {
        mutableStateOf(false)
    }

    AppTheme {
        Dialog(onDismissRequest = onDismiss) {
            Surface(
                shape = MaterialTheme.shapes.extraLarge,
                tonalElevation = 6.dp,
                modifier = Modifier
                    .width(IntrinsicSize.Min)
                    .height(IntrinsicSize.Min)
                    .background(
                        shape = MaterialTheme.shapes.extraLarge,
                        color = MaterialTheme.colorScheme.tertiary
                    )
            ) {
                Column(modifier = Modifier.padding(30.dp)) {
                    TimePicker(state = state)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(45.dp)
                    ) {
                        Spacer(modifier = Modifier.weight(2f))
                        Button(
                            onClick = { onDismiss() },
                            enabled = true
                        ) {
                            Text(text = "Cancel")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                val formattedHour = if (state.hour == 0 || state.hour == 12) "12" else String.format(
                                    "%02d",
                                    state.hour % 12
                                )
                                val paddedMinute = String.format("%02d", state.minute)
                                val period = if (state.hour < 12) "AM" else "PM"
                                onConfirm("$formattedHour:$paddedMinute $period")

                                val selectedTimes = medicationViewModel.timeStates.map { timeState ->
                                    Time(timeState.hour, timeState.minute, 0)
                                }.toMutableList().dropLast(1)

                                if (selectedTimes.any { it == Time(state.hour, state.minute, 0) }) {
                                    dialogState = true
                                    println("Dialog State: $dialogState")
                                    return@Button
                                }
                                onDismiss()
                            },
                            enabled = true
                        ) {
                            Text(text = "OK")
                        }
                    }
                }
            }
            if (dialogState) {
                AlertDialog(
                    onDismissRequest = { dialogState = false },
                    title = { Text(text = "Error") },
                    text = { Text(text = "The selected time has already been added.") },
                    confirmButton = {
                        Button(
                            onClick = { dialogState = false },
                        ) {
                            Text(text = "OK")
                        }
                    }
                )
            }
        }
    }
}
