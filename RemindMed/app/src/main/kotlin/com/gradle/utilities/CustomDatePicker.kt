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
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import com.gradle.ui.theme.AppTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePicker(state: DatePickerState, date: String) {
    var date by remember {
        mutableStateOf(date)
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
                ButtonSecondary(text = "Ok", onClick = {
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
    formatter.timeZone = TimeZone.getTimeZone("UTC")
    return formatter.format(Date(millis))
}

//import android.widget.DatePicker
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.width
//import androidx.compose.material3.Button
//import androidx.compose.material3.Text
//import androidx.compose.material3.ButtonColors
//import androidx.compose.material3.DatePicker
//import androidx.compose.material3.DatePickerDefaults
//import androidx.compose.material3.DatePickerDialog
//import androidx.compose.material3.DatePickerState
//import androidx.compose.material3.DateRangePicker
//import androidx.compose.material3.DateRangePickerDefaults
//import androidx.compose.material3.DateRangePickerState
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.OutlinedButton
//import androidx.compose.material3.rememberDatePickerState
//
//
//import androidx.compose.material3.rememberDateRangePickerState
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.window.Dialog
//import com.gradle.ui.components.ButtonPrimary
//import com.gradle.ui.components.ButtonSecondary
//import com.gradle.ui.theme.AppTheme
//import java.text.SimpleDateFormat
//import java.util.Date
//import java.util.TimeZone
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun CustomDatePicker(
//    state: DateRangePickerState,
//    onDateSelected: (String, String) -> Unit
//) {
//    var openDialog by remember { mutableStateOf(false) }
////    val startState = rememberDateRangePickerState()
////    val endState = rememberDateRangePickerState()
//
//    AppTheme {
//        Box(contentAlignment = Alignment.Center) {
//            OutlinedButton(
//                onClick = { openDialog = true },
//                colors = ButtonColors(
//                    containerColor = MaterialTheme.colorScheme.tertiary,
//                    contentColor = MaterialTheme.colorScheme.primary,
//                    disabledContainerColor = MaterialTheme.colorScheme.tertiary,
//                    disabledContentColor = MaterialTheme.colorScheme.tertiary
//                ),
//                enabled = true
//            ) {
//                Text(text = "Select Dates")
//            }
//        }
//    }
//
//    if (openDialog) {
//        MyDatePickerDialog(
//            state = state,
//            onDateSelected = { start, end -> // Correctly handle the selected dates
//                onDateSelected(start, end)
//            },
//
//            onDismiss = { openDialog = false }
//        )
//    }
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun MyDatePickerDialog(
//    state: DateRangePickerState,
//    onDateSelected: (String, String) -> Unit,
//    onDismiss: () -> Unit
//) {
//    val startDate = state.selectedStartDateMillis?.let { convertMillisToDate(it) } ?: ""
//
//    val endDate = state.selectedEndDateMillis?.let { convertMillisToDate(it) } ?: ""
//
////    AppTheme {
////        DatePickerDialog(onDismissRequest = onDismiss, confirmButton = ()) {
////            Column(
////                modifier = Modifier.padding(16.dp),
////                verticalArrangement = Arrangement.spacedBy(16.dp)
////            ) {
////                DateRangePicker(state = state)
////                Row(
////                    modifier = Modifier.fillMaxWidth(),
////                    horizontalArrangement = Arrangement.End
////                ) {
////                    ButtonSecondary(text = "Cancel", onClick = { onDismiss() }, enabled = true)
////                    Spacer(modifier = Modifier.width(8.dp))
////                    ButtonSecondary(
////                        text = "Ok",
////                        onClick = {
////                            // Call the callback with selected start and end dates
////                            onDateSelected(startDate, endDate)
////                            onDismiss()
////                        },
////                        enabled = true
////                    )
////                }
////            }
////        }
////    }
//    AppTheme {
//        DatePickerDialog(
//            onDismissRequest = { onDismiss() },
//            confirmButton = {
//                ButtonSecondary(text = "Ok", onClick = {
//                    onDateSelected(startDate, endDate)
//                    onDismiss()
//                }, enabled = true)
//            },
//            dismissButton = {
//                ButtonSecondary(text = "Cancel", onClick = { onDismiss() }, enabled = true)
//            }
//        ) {
//            DateRangePicker(
//                state = state,
//                headline = {
//                    DateRangePickerDefaults.DateRangePickerHeadline(
//                        selectedStartDateMillis = state.selectedStartDateMillis,
//                        selectedEndDateMillis = state.selectedEndDateMillis,
//                        displayMode = state.displayMode,
//                        dateFormatter = remember { DatePickerDefaults.dateFormatter() },
//                        modifier = Modifier.padding(12.dp)
//                    )
//                },
////                modifier = Modifier.padding(12.dp),
//            )
//        }
//    }
//
//}
//
//private fun convertMillisToDate(millis: Long): String {
//    val formatter = SimpleDateFormat("dd/MM/yyyy")
//    formatter.timeZone = TimeZone.getTimeZone("UTC")
//    return formatter.format(Date(millis))
//}


//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material.Icon
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Close
//import androidx.compose.material3.DatePicker
//import androidx.compose.material3.DatePickerDefaults
//import androidx.compose.material3.DateRangePicker
//import androidx.compose.material3.DateRangePickerState
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.SnackbarHost
//import androidx.compose.material3.SnackbarHostState
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextButton
//import androidx.compose.material3.rememberDateRangePickerState
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.zIndex
//import kotlinx.coroutines.launch
//
//// Decoupled snackbar host state from scaffold state for demo purposes.
//
//@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
//@Composable
//fun CustomDatePicker(state: DateRangePickerState) {
//    val snackState = remember { SnackbarHostState() }
//    val snackScope = rememberCoroutineScope()
//    SnackbarHost(hostState = snackState, Modifier.zIndex(1f))
//
//    val state = rememberDateRangePickerState()
//    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top) {
//        // Add a row with "Save" and dismiss actions.
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(DatePickerDefaults.colors().containerColor)
//                .padding(start = 12.dp, end = 12.dp),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            IconButton(onClick = { /* dismiss the UI */ }) {
//                Icon(Icons.Filled.Close, contentDescription = "Localized description")
//            }
//            TextButton(
//                onClick = {
//                    snackScope.launch {
//                        val range =
//                            state.selectedStartDateMillis!!..state.selectedEndDateMillis!!
//                        snackState.showSnackbar("Saved range (timestamps): $range")
//                    }
//                },
//                enabled = state.selectedEndDateMillis != null
//            ) {
//                Text(text = "Save")
//            }
//        }
//        DateRangePicker(state = state, modifier = Modifier.weight(1f))
//    }
//}