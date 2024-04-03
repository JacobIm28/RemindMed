package com.gradle.ui.views.patient
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.ui.graphics.Color
//import com.gradle.models.Medication
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.gradle.models.CalendarModel
import com.gradle.utilities.CalendarDataSource
import com.gradle.utilities.toFormattedDateShortString
import com.gradle.utilities.toFormattedDateString
import com.gradle.utilities.toFormattedMonthDateString
import com.example.remindmed.R
import com.gradle.constants.GlobalObjects
import com.gradle.models.DateModel
import com.gradle.models.Medication
import com.gradle.models.Patient
import com.gradle.ui.theme.AppTheme
import java.time.LocalDateTime
//import com.google.accompanist.permissions.ExperimentalPermissionsApi
//import com.google.accompanist.permissions.isGranted
//import com.google.accompanist.permissions.rememberPermissionState
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit

import com.gradle.apiCalls.PatientApi as PatientApi


class Prescription (
    val name: String,
    val amount: String,
    val times: List<String>
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
        HorizontalDivider(modifier = Modifier.padding(8.dp))
        takenMedications.forEach { medication ->
            MedicationItem(prescription = medication, taken = true, onCheckedChange = { isChecked ->
                onCheckedChange(medication, isChecked)
            })
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Not Finished:", modifier = Modifier.padding(8.dp), style = MaterialTheme.typography.titleMedium)
        HorizontalDivider(modifier = Modifier.padding(8.dp))
        notTakenMedications.forEach { medication ->
            MedicationItem(prescription = medication, taken = false, onCheckedChange = { isChecked ->
                onCheckedChange(medication, isChecked)
            })
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
//fun HomeScreen(viewModel: HomepageViewModel, controller: HomepageController) {
fun HomeScreen() {
    var takenMedications by remember { mutableStateOf(listOf<Prescription>()) }
    var notTakenMedications by remember { mutableStateOf(listOf<Prescription>()) }
    var medicationsToday by remember { mutableStateOf(listOf<Medication>()) }
    var selectedDate by remember { mutableStateOf(Date()) }

    var medications by remember { mutableStateOf(emptyList<Medication>()) }
    var patient by remember { mutableStateOf(Patient("")) }
    var greeting by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        selectedDate = Calendar.getInstance().time
        patient = PatientApi().getPatientbyId(GlobalObjects.patient.pid)
        medications = PatientApi().getMedicines(GlobalObjects.patient.pid)
        medicationsToday = medications.filter { medication ->
            (selectedDate >= medication.startDate && selectedDate < Date(medication.endDate.time + TimeUnit.DAYS.toMillis(1)))
        }
        greeting = when (LocalDateTime.now().hour) {
            in 0..11 -> "Good Morning"
            in 12..16 -> "Good Afternoon"
            else -> "Good Evening"
        }
    }
    notTakenMedications = medicationsToday.map { medication ->
        Prescription(medication.name, medication.amount, medication.getFormattedTimes())
    }
    takenMedications = emptyList()

    // some weird issue here with slight UI lag, come back to this
//    takenMedications = emptyList() // Clear the taken medications list
//    LaunchedEffect(selectedDate) {
//        println(selectedDate)
//        medications = PatientApi().getMedicines(GlobalObjects.patient.pid)
//        medicationsToday = medications.filter { medication ->
//            (selectedDate >= medication.startDate  && selectedDate <= medication.endDate)
//        }
//        notTakenMedications = medicationsToday.map { medication ->
//            Prescription(medication.name, medication.amount, medication.times.toString())
//        }
//        takenMedications = emptyList() // Clear the taken medications list
//    }
    AppTheme {
        Scaffold(
        ) { innerPadding ->
            Box(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Column(modifier = Modifier.padding(innerPadding)) {

                    DatesHeader(
                        onDateSelected = { newSelectedDate ->
                            selectedDate =
                                newSelectedDate // LOW PRIORITY: need to restrict it so that medications for days other than today cannot be marked taken,
                            // user can only "take" medications for the current date, selecting a new date just allows for user to see what medications they have for that day
                        }
                    )
                    DailyOverviewCard(medicationsToday, greeting, patient)
                    Spacer(modifier = Modifier.height(16.dp))

                    MedicationListHomeScreen(
                        takenMedications = takenMedications,
                        notTakenMedications = notTakenMedications
                    ) { medication, isChecked ->
                        if (isChecked) {
                            notTakenMedications = notTakenMedications.filterNot { it == medication }
                            takenMedications = takenMedications + medication
                        } else {
                            takenMedications = takenMedications.filterNot { it == medication }
                            notTakenMedications = notTakenMedications + medication
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyOverviewCard(
    medicationsToday: List<Medication>,
    greeting: String,
    patient: Patient
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
                    text = "${greeting} ${patient.name.substringBefore(" ")},",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                )
                if (medicationsToday.isEmpty()) {
                    Text(
                        text = "No medications scheduled for this date. Keep it up!",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                    )
                } else {
                    Text(
                        text = "Check off the medications that you have taken today.",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
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
    onDateSelected: (Date) -> Unit
) {
    var dataSource by remember { mutableStateOf(CalendarDataSource())}
    var calendarModel by remember { mutableStateOf(dataSource.getData(lastSelectedDate = dataSource.today)) }

    LaunchedEffect(Unit) {
        dataSource = CalendarDataSource()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp)
    ) {
        DateHeader(
            data = calendarModel,
            onPrevClickListener = { startDate ->
                val calendar = Calendar.getInstance().apply {
                    time = startDate
                    add(Calendar.DAY_OF_YEAR, -2)
                }
                val finalStartDate = calendar.time
                calendarModel = dataSource.getData(startDate = finalStartDate, lastSelectedDate = calendarModel.selectedDate.date)
            },
            onNextClickListener = { endDate ->
                val calendar = Calendar.getInstance().apply {
                    time = endDate
                    add(Calendar.DAY_OF_YEAR, 2)
                }
                val finalStartDate = calendar.time
                calendarModel = dataSource.getData(startDate = finalStartDate, lastSelectedDate = calendarModel.selectedDate.date)
            }
        )
        DateList(
            data = calendarModel,
            onDateClickListener = { dateModel ->
                calendarModel = calendarModel.copy(
                    selectedDate = dateModel,
                    visibleDates = calendarModel.visibleDates.map { visibleDate ->
                        visibleDate.copy(
                            isSelected = visibleDate.date.toFormattedDateString() == dateModel.date.toFormattedDateString()
                        )
                    }
                )
                onDateSelected(dateModel.date)
            }
        )
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

@Composable
fun DateList(
    data: CalendarModel,
    onDateClickListener: (DateModel) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        items(items = data.visibleDates) { date ->
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
                        .padding(horizontal = 2.dp),
                    onClick = { onDateClickListener(date) },
                    colors = cardColors(
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
                            .padding(2.dp)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = date.date.toFormattedDateShortString(),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                }
                Text(
                    text = data.monthNames[date.date.month.plus(1)] ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )
            }

        }
    }
}