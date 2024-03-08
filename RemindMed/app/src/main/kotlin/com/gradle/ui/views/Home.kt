package com.gradle.ui.views
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
//import com.gradle.models.Medication
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
//import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.gradle.models.CalendarModel
import com.gradle.utilities.CalendarDataSource
import com.gradle.utilities.toFormattedDateShortString
import com.gradle.utilities.toFormattedDateString
import com.gradle.utilities.toFormattedMonthDateString
import com.example.remindmed.R
import com.gradle.ui.theme.AppTheme
//import com.google.accompanist.permissions.ExperimentalPermissionsApi
//import com.google.accompanist.permissions.isGranted
//import com.google.accompanist.permissions.rememberPermissionState
import java.util.Calendar
import java.util.Date

data class HomeState(
    val greeting: String = "",
    val userName: String = "",
    val medications: List<Medication> = emptyList()
)

class Prescription (
    val name: String,
    val amount: String,
    val times: String
)

@Composable
fun MedicationItem(
    prescription: Prescription,
    taken: Boolean = false,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.LightGray, RoundedCornerShape(15.dp)),

        colors = cardColors(
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.primary,
        ),

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(prescription.name, fontWeight = FontWeight.Bold)
                Text("Dosage: ${prescription.amount}", style = MaterialTheme.typography.bodyMedium)
                Text("Time: ${prescription.times}", style = MaterialTheme.typography.bodyMedium)
            }
            Checkbox(
                checked = taken,
                onCheckedChange = onCheckedChange,
                colors = CheckboxDefaults.colors(checkmarkColor = Color.White),
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun MedicationListHomeScreen(
    takenMedications: List<Prescription>,
    notTakenMedications: List<Prescription>,
    onCheckedChange: (Prescription, Boolean) -> Unit
) {

    Column {
        Text("Taken:", modifier = Modifier.padding(8.dp),style = MaterialTheme.typography.titleMedium)
        takenMedications.forEach { medication ->
            MedicationItem(prescription = medication, taken = true, onCheckedChange = { isChecked ->
                onCheckedChange(medication, isChecked)
            })
        }
        Text("Not Finished:", modifier = Modifier.padding(8.dp), style = MaterialTheme.typography.titleMedium)
        notTakenMedications.forEach { medication ->
            MedicationItem(prescription = medication, taken = false, onCheckedChange = { isChecked ->
                onCheckedChange(medication, isChecked)
            })
        }
    }
}

@Composable
fun HomeScreen(
    navController: NavController
) {
    var takenMedications by remember { mutableStateOf(listOf<Prescription>()) }
    var notTakenMedications by remember { mutableStateOf(listOf<Prescription>()) }

    val (initialTakenMedications, initialNotTakenMedications) = remember {
        // TODO: REPLACE WITH THE ACTUAL DATA FROM DATABASE FOR TODAY
        Pair(
            listOf(
                Prescription("Reserphine", "3 ml per usage", "9:00 AM"),
                Prescription("Advil", "One every 6 hours", "Whenever back starts to hurt")
            ),
            listOf(
                Prescription("Vitamin D", "4g twice per day", "9:00 AM + 12:00 AM")
            )
        )
    }

    takenMedications = initialTakenMedications
    notTakenMedications = initialNotTakenMedications

    AppTheme {
        Scaffold(
        ) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {

                DatesHeader(
                    onDateSelected = {
            // TODO: Hook up to the actual data from the database for selected date
            //                    selectedDate ->
//                        val newMedicationList = state.medications
//                            .filter { medication ->
//                                medication.medicationTime.toFormattedDateString() == selectedDate.date.toFormattedDateString()
//                            }
//                            .sortedBy { it.medicationTime }
//
//                        filteredMedications = newMedicationList
                    }
                )
                DailyOverviewCard(medicationsToday = emptyList(), logEvent = {})
                Spacer(modifier = Modifier.height(16.dp))
                MedicationListHomeScreen(
                    takenMedications = takenMedications,
                    notTakenMedications = notTakenMedications
                ) { medication, isChecked ->
                    if (isChecked) {
                        // Move medication from not taken to taken
                        notTakenMedications = notTakenMedications.filterNot { it == medication }
                        takenMedications = takenMedications + medication
                    } else {
                        // Move medication from taken to not taken
                        takenMedications = takenMedications.filterNot { it == medication }
                        notTakenMedications = notTakenMedications + medication
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun PreviewHomeScreen() {
    HomeScreen(navController = rememberNavController())
}

@Composable
fun Greeting() {
    Column {
        // NOT SURE IF NECESSARY BUT SOMETHING WE CAN DO
        // TODO: Add greeting based on time of day e.g. Good Morning, Good Afternoon, Good evening.
        // TODO: Get name from DB and show user's first name.
        Text(
            text = "Good morning,",
            style = MaterialTheme.typography.displaySmall
        )
        Text(
            text = "Kathryn!",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.displaySmall
        )
        Spacer(modifier = Modifier.padding(8.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyOverviewCard(
    medicationsToday: List<Medication>,
    logEvent: (String) -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .height(156.dp),
        shape = RoundedCornerShape(15.dp),
        colors = cardColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = Color.White
        ),
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp, 24.dp, 0.dp, 16.dp)
                    .fillMaxWidth(.50F)
                    .align(Alignment.CenterVertically),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {

                Text(
                    text = "Check off the medications that you have taken today ",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Bottom
            ) {
                Image(
                    painter = painterResource(id = R.drawable.doctor), contentDescription = ""
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyOverviewEmptyCard() {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(156.dp),
        shape = RoundedCornerShape(36.dp),
        colors = cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.primary
        ),
        onClick = {
        }
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(24.dp, 24.dp, 0.dp, 16.dp)
                    .fillMaxWidth(.50F)
                    .align(Alignment.CenterVertically),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {

                Text(
                    text = "Medication Break",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge,
                )

                Text(
                    text = "No medications scheduled for this date. Take a break and relax.",
                    style = MaterialTheme.typography.titleSmall,
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Bottom
            ) {
                Image(
                    painter = painterResource(id = R.drawable.doctor), contentDescription = ""
                )
            }
        }
    }
}

@Composable
fun DatesHeader(
    onDateSelected: (CalendarModel.DateModel) -> Unit // Callback to pass the selected date
) {
    val dataSource = CalendarDataSource()
    var calendarModel by remember { mutableStateOf(dataSource.getData(lastSelectedDate = dataSource.today)) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp)
    ) {
        DateHeader(
            data = calendarModel,
            onPrevClickListener = { startDate ->
                // refresh the CalendarModel with new data
                // by get data with new Start Date (which is the startDate-1 from the visibleDates)
                val calendar = Calendar.getInstance()
                calendar.time = startDate

                calendar.add(Calendar.DAY_OF_YEAR, -2) // Subtract one day from startDate
                val finalStartDate = calendar.time

                calendarModel = dataSource.getData(startDate = finalStartDate, lastSelectedDate = calendarModel.selectedDate.date)
            },
            onNextClickListener = { endDate ->
                // refresh the CalendarModel with new data
                // by get data with new Start Date (which is the endDate+2 from the visibleDates)
                val calendar = Calendar.getInstance()
                calendar.time = endDate

                calendar.add(Calendar.DAY_OF_YEAR, 2)
                val finalStartDate = calendar.time

                calendarModel = dataSource.getData(startDate = finalStartDate, lastSelectedDate = calendarModel.selectedDate.date)
            }
        )
        DateList(
            data = calendarModel,
            onDateClickListener = { date ->
                calendarModel = calendarModel.copy(
                    selectedDate = date,
                    visibleDates = calendarModel.visibleDates.map {
                        it.copy(
                            isSelected = it.date.toFormattedDateString() == date.date.toFormattedDateString()
                        )
                    }
                )
                onDateSelected(date)
            }
        )
    }
}

@Composable
fun DateList(
    data: CalendarModel,
    onDateClickListener: (CalendarModel.DateModel) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        items(items = data.visibleDates) { date ->
            DateItem(date, onDateClickListener)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateItem(
    date: CalendarModel.DateModel,
    onClickListener: (CalendarModel.DateModel) -> Unit,
) {
    Column {
        Text(
            text = date.day, // day "Mon", "Tue"
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Card(
            modifier = Modifier
                .padding(vertical = 4.dp, horizontal = 2.dp),
            onClick = { onClickListener(date) },
            colors = cardColors(
                // background colors of the selected date
                // and the non-selected date are different
                containerColor = if (date.isSelected) {
                    MaterialTheme.colorScheme.secondary
                } else {
                    MaterialTheme.colorScheme.surface
                }
            ),
        ) {
            Column(
                modifier = Modifier
                    .width(42.dp)
                    .height(42.dp)
                    .padding(8.dp)
                    .fillMaxSize(), // Fill the available size in the Column
                verticalArrangement = Arrangement.Center, // Center vertically
                horizontalAlignment = Alignment.CenterHorizontally // Center horizontally
            ) {
                Text(
                    text = date.date.toFormattedDateShortString(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = if (date.isSelected) {
                        FontWeight.Bold
                    } else {
                        FontWeight.Bold
                    }
                )
            }
        }
    }
}

@Composable
fun DateHeader(
    data: CalendarModel,
    onPrevClickListener: (Date) -> Unit,
    onNextClickListener: (Date) -> Unit
) {
    Row(
        modifier = Modifier.padding(vertical = 16.dp),
    ) {
        Text(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically),
            text = if (data.selectedDate.isToday) {
                "Today"
            } else {
                data.selectedDate.date.toFormattedMonthDateString()
            },
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        IconButton(onClick = {
            onPrevClickListener(data.startDate.date)
        }) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowLeft,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = "Back"
            )
        }
        IconButton(onClick = {
            onNextClickListener(data.endDate.date)
        }) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowRight,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = "Next"
            )
        }
    }
}

sealed class MedicationListItem {
    data class OverviewItem(val medicationsToday: List<Medication>, val isMedicationListEmpty: Boolean) : MedicationListItem()
    data class MedicationItem(val medication: Medication) : MedicationListItem()
    data class HeaderItem(val headerText: String) : MedicationListItem()
}

//@Composable
//fun DailyMedications(
//    navController: NavController,
//    state: HomeState,
//    navigateToMedicationDetail: (Medication) -> Unit,
//    logEvent: (String) -> Unit
//) {
//
//    var filteredMedications: List<Medication> by remember { mutableStateOf(emptyList()) }
//
//    DatesHeader(
//        onDateSelected = { selectedDate ->
////            val newMedicationList = state.medications
////                .filter { medication ->
////                    medication.medicationTime.toFormattedDateString() == selectedDate.date.toFormattedDateString()
////                }
////                .sortedBy { it.medicationTime }
//
////            filteredMedications = newMedicationList
//        }
//    )
//
//    if (filteredMedications.isEmpty()) {
//        EmptyCard(
//            navController = navController,
//        )
//    } else {
//        LazyColumn(
//            modifier = Modifier,
//        ) {
////            items(
////                items = filteredMedications,
////                itemContent = {
////                    MedicationSummaryCard(
////                        medication = it,
////                        navigateToMedicationDetail = { medication ->
////                            navigateToMedicationDetail(medication)
////                        }
////                    )
////                }
////            )
//        }
//    }
//}

//@OptIn(ExperimentalPermissionsApi::class)
//@Composable
//fun PermissionDialog(
//    askNotificationPermission: Boolean,
//    logEvent: (String) -> Unit
//) {
//    if (askNotificationPermission && (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)) {
//        val notificationPermissionState = rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS) { isGranted ->
//            when (isGranted) {
//                true -> logEvent.invoke(AnalyticsEvents.NOTIFICATION_PERMISSION_GRANTED)
//                false -> logEvent.invoke(AnalyticsEvents.NOTIFICATION_PERMISSION_REFUSED)
//            }
//        }
//        if (!notificationPermissionState.status.isGranted) {
//            val openAlertDialog = remember { mutableStateOf(true) }
//
//            when {
//                openAlertDialog.value -> {
//                    logEvent.invoke(AnalyticsEvents.NOTIFICATION_PERMISSION_DIALOG_SHOWN)
//                    AlertDialog(
//                        icon = {
//                            Icon(
//                                imageVector = Icons.Default.Notifications,
//                                contentDescription = stringResource(R.string.notifications)
//                            )
//                        },
//                        title = {
//                            Text(text = stringResource(R.string.notification_permission_required))
//                        },
//                        text = {
//                            Text(text = stringResource(R.string.notification_permission_required_description_message))
//                        },
//                        onDismissRequest = {
//                            openAlertDialog.value = false
//                            logEvent.invoke(AnalyticsEvents.NOTIFICATION_PERMISSION_DIALOG_DISMISSED)
//                        },
//                        confirmButton = {
//                            Button(
//                                onClick = {
//                                    notificationPermissionState.launchPermissionRequest()
//                                    openAlertDialog.value = false
//                                    logEvent.invoke(AnalyticsEvents.NOTIFICATION_PERMISSION_DIALOG_ALLOW_CLICKED)
//                                }
//                            ) {
//                                Text(stringResource(R.string.allow))
//                            }
//                        }
//                    )
//                }
//            }
//        }
//    }
//}

// @OptIn(ExperimentalPermissionsApi::class)
// @Composable
// fun PermissionAlarmDialog(
//     askAlarmPermission: Boolean,
//     logEvent: (String) -> Unit
// ) {
//     val context = LocalContext.current
//     val alarmManager = ContextCompat.getSystemService(context, AlarmManager::class.java)
//     if (askAlarmPermission && (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE)) {
//         val alarmPermissionState = rememberPermissionState(Manifest.permission.SCHEDULE_EXACT_ALARM) { isGranted ->
//             when (isGranted) {
//                 true -> logEvent.invoke(AnalyticsEvents.ALARM_PERMISSION_GRANTED)
//                 false -> logEvent.invoke(AnalyticsEvents.ALARM_PERMISSION_REFUSED)
//             }
//         }
//         if (alarmManager?.canScheduleExactAlarms() == false) {
//             val openAlertDialog = remember { mutableStateOf(true) }

//             when {
//                 openAlertDialog.value -> {

//                     logEvent.invoke(AnalyticsEvents.ALARM_PERMISSION_DIALOG_SHOWN)

//                     AlertDialog(
//                         icon = {
//                             Icon(
//                                 imageVector = Icons.Default.Notifications,
//                                 contentDescription = stringResource(R.string.alarms)
//                             )
//                         },
//                         title = {
//                             Text(text = stringResource(R.string.alarms_permission_required))
//                         },
//                         text = {
//                             Text(text = stringResource(R.string.alarms_permission_required_description_message))
//                         },
//                         onDismissRequest = {
//                             openAlertDialog.value = false
//                             logEvent.invoke(AnalyticsEvents.ALARM_PERMISSION_DIALOG_DISMISSED)
//                         },
//                         confirmButton = {
//                             Button(
//                                 onClick = {
//                                     Intent().also { intent ->
//                                         intent.action = Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
//                                         context.startActivity(intent)
//                                     }

//                                     openAlertDialog.value = false
//                                     logEvent.invoke(AnalyticsEvents.ALARM_PERMISSION_DIALOG_ALLOW_CLICKED)
//                                 }
//                             ) {
//                                 Text(stringResource(R.string.allow))
//                             }
//                         }
//                     )
//                 }
//             }
//         }
//     }
// }
