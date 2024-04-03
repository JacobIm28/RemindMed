package com.gradle.ui.views

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.gradle.constants.Routes
import com.gradle.ui.theme.AppTheme
import com.gradle.constants.*
import com.gradle.controller.DoctorController
import com.gradle.controller.MedicationController
import com.gradle.controller.PatientController
import com.gradle.models.LoginModel
import com.gradle.models.Medication
import com.gradle.ui.viewModels.DoctorViewModel
import com.gradle.ui.viewModels.MedicationViewModel
import com.gradle.ui.viewModels.PatientViewModel
import com.gradle.ui.views.doctor.AddPatientScreen
import com.gradle.ui.views.patient.HomeScreen
import com.gradle.ui.views.shared.MedicationEntryScreen
import com.gradle.ui.views.shared.MedicationEditScreen
import com.gradle.ui.views.shared.MedicationInfoScreen
import com.gradle.ui.views.shared.MedicationListScreen
import com.gradle.ui.views.shared.PeopleListScreen
import com.gradle.ui.views.shared.ProfileScreen
import java.sql.Date
import java.sql.Time

data class NavigationItem(
    val icon: ImageVector,
    val label: String,
    val route: String
)

@RequiresApi(Build.VERSION_CODES.S)
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("RememberReturnType", "UnusedMaterialScaffoldPaddingParameter")
@Composable
fun RemindMedApp(context: Context, loginModel: LoginModel) {
    val navController = rememberNavController();
    val context = LocalContext.current
    val CHANNEL_ID = "CHANNEL"

    val patientNavBarItems = arrayOf(
        NavigationItem(
            icon = Icons.Rounded.Home,
            label = "Home",
            route = Routes.HOME
        ),
        NavigationItem(
            icon = Icons.Rounded.List,
            label = "Medications",
            route = Routes.MEDICATION_LIST + "?${NavArguments.MEDICATION_LIST.PID}=${GlobalObjects.patient.pid}"
        ),
        NavigationItem(
            icon = Icons.Rounded.Person,
            label = "Doctors",
            route = Routes.PEOPLE_LIST
        ),
        NavigationItem(
            icon = Icons.Rounded.AccountCircle,
            label = "Profile",
            route = Routes.PROFILE
        )
    )

    val doctorNavBarItems = arrayOf(
        NavigationItem(
            icon = Icons.Rounded.Person,
            label = "Patients",
            route = Routes.PEOPLE_LIST,
        ),
        NavigationItem(
            icon = Icons.Default.Add,
            label = "Add Patients",
            route = Routes.ADD_PATIENT
        ),
        NavigationItem(
            icon = Icons.Rounded.AccountCircle,
            label = "Profile",
            route = Routes.PROFILE
        )
    )

    val navBarItems = if (GlobalObjects.type == "doctor") doctorNavBarItems else patientNavBarItems

    fun onNavigateToMedicationList(pid: String) {
        navController.navigate(Routes.MEDICATION_LIST + "?" +
                "${NavArguments.MEDICATION_LIST.PID}=${pid}")
    }

    fun onNavigateToPeopleList() {
        navController.navigate(Routes.PEOPLE_LIST)
    }

    fun onNavigateToMedicationEntry(pid: String) {
        navController.navigate(Routes.MEDICATION_ENTRY + "?" +
                "${NavArguments.MEDICATION_ENTRY.PID}=${pid}"
        )
    }

    fun onNavigateToMedicationEdit(medication: Medication) {
        navController.navigate(Routes.MEDICATION_EDIT + "?" +
                "${NavArguments.MEDICATION_EDIT.MEDICATION_ID}=${medication.medicationId}" +
                "&${NavArguments.MEDICATION_LIST.PID}=${medication.pid}"
        )
    }

    fun onNavigateToMedicationInfo(medication: Medication) {
        navController.navigate(
            Routes.MEDICATION_INFO + "?${NavArguments.MEDICATION_INFO.MEDICATION_NAME}=${medication.name}&" +
                    "${NavArguments.MEDICATION_INFO.START_DATE}=${medication.startDate}&" +
                    "${NavArguments.MEDICATION_INFO.END_DATE}=${medication.endDate}&" +
                    "${NavArguments.MEDICATION_INFO.DOSAGE}=${medication.amount}"
        )
    }

    AppTheme {
        // Routing
        Scaffold (
            bottomBar = {
                BottomNavigation (
                    backgroundColor = MaterialTheme.colorScheme.tertiary
                ) {

                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination

                    navBarItems.forEach {navItem ->
                        BottomNavigationItem(
                            icon = { Icon(navItem.icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                            label = { Text(navItem.label, style = MaterialTheme.typography.bodySmall, maxLines = 1) },
                            selected = currentDestination?.hierarchy?.any { it.route == navItem.route} == true,
                            onClick = {
                                navController.navigate(navItem.route)
                            }
                        )
                    }
                }
            }
        ) { innerPadding ->
            Column (
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(25.dp)
            ) {
                NavHost(navController, startDestination = if (GlobalObjects.type == "doctor") Routes.PEOPLE_LIST else Routes.HOME ) {
                    composable(Routes.PEOPLE_LIST) {
                        PeopleListScreen(onNavigateToMedicationList = { pid: String -> onNavigateToMedicationList(pid)})
                    }

                    composable(
                        Routes.MEDICATION_LIST_WITH_ARGS,
                        arguments = listOf(navArgument(NavArguments.MEDICATION_LIST.PID) { type = NavType.StringType })
                    ) {backStackEntry ->
                        MedicationListScreen(
                            pid = backStackEntry.arguments?.getString(NavArguments.MEDICATION_LIST.PID)?: "",
                            onNavigateToMedicationEntry = { pid: String -> onNavigateToMedicationEntry(pid) },
                            onNavigateToMedicationEdit = { medication: Medication -> onNavigateToMedicationEdit(medication) },
                            onNavigateToMedicationInfo = { medication: Medication -> onNavigateToMedicationInfo(medication) }
                        )

                    }

//                    if (GlobalObjects.type == "patient") {
//                        val patientModel = PatientViewModel(GlobalObjects.patient)
//                        val patientController = PatientController(GlobalObjects.patient)
//                        composable(Routes.PROFILE) { ProfileScreen(navController, patientModel, patientController) }
//                    } else {
//                        val doctorModel = DoctorViewModel(GlobalObjects.doctor)
//                        val doctorController = DoctorController(GlobalObjects.doctor)
//                        composable(Routes.PROFILE) { ProfileScreen(navController, doctorModel, doctorController) }
//                    }
                    if (GlobalObjects.type == "patient") {
                        val patientModel = PatientViewModel(GlobalObjects.patient)
                        val patientController = PatientController(GlobalObjects.patient, loginModel)
                        composable(Routes.PROFILE) { ProfileScreen(patientModel, patientController, loginModel) }
                    } else {
                        val doctorModel = DoctorViewModel(GlobalObjects.doctor)
                        val doctorController = DoctorController(GlobalObjects.doctor, loginModel)
                        composable(Routes.PROFILE) { ProfileScreen(doctorModel, doctorController, loginModel) }
                    }

                    composable(
                        Routes.MEDICATION_INFO_WITH_ARGS,
                        arguments = listOf(
                            navArgument(NavArguments.MEDICATION_INFO.MEDICATION_NAME) { type = NavType.StringType; defaultValue = "" },
                            navArgument(NavArguments.MEDICATION_INFO.START_DATE) { type = NavType.StringType; defaultValue = "" },
                            navArgument(NavArguments.MEDICATION_INFO.END_DATE) { type = NavType.StringType; defaultValue = "" },
                            navArgument(NavArguments.MEDICATION_INFO.DOSAGE) { type = NavType.StringType; defaultValue = "" }
                        )
                    ) { backStackEntry ->
                        MedicationInfoScreen(
                            medicationName = backStackEntry.arguments?.getString(NavArguments.MEDICATION_INFO.MEDICATION_NAME)?: "",
                            startDate = backStackEntry.arguments?.getString(NavArguments.MEDICATION_INFO.START_DATE)?: "",
                            endDate = backStackEntry.arguments?.getString(NavArguments.MEDICATION_INFO.END_DATE)?: "",
                            dosage = backStackEntry.arguments?.getString(NavArguments.MEDICATION_INFO.DOSAGE)?: ""
                        )
                    }

                    var nullMedication = Medication("-1", "", "", Date(0), Date(0), "", "", mutableListOf<Time>()) // null medication for entry

                    if (GlobalObjects.type == "patient") {
                        nullMedication = Medication(GlobalObjects.patient.pid, "", "", Date(0), Date(0), "", "", mutableListOf<Time>())
                    }

                    val medicationModel = MedicationViewModel(nullMedication) // null medication for entry
                    val medicationController = MedicationController(nullMedication)

                    composable(
                        Routes.MEDICATION_ENTRY_WITH_ARGS,
                        arguments = listOf(
                            navArgument(NavArguments.MEDICATION_ENTRY.PID) { type = NavType.StringType; defaultValue = "" }
                        )
                    ) {
                        MedicationEntryScreen(
                            onNavigateToPeopleList = { onNavigateToPeopleList() },
                            onNavigateToMedicationList = { pid: String -> onNavigateToMedicationList(pid) },
                            medicationViewModel = medicationModel,
                            medicationController = medicationController,
                            pid = it.arguments?.getString(NavArguments.MEDICATION_ENTRY.PID)?: ""
                        )
                    }

                    composable(
                        Routes.MEDICATION_INFO_WITH_ARGS,
                        arguments = listOf(
                            navArgument(NavArguments.MEDICATION_INFO.MEDICATION_NAME) { type = NavType.StringType; defaultValue = "" },
                            navArgument(NavArguments.MEDICATION_INFO.START_DATE) { type = NavType.StringType; defaultValue = "" },
                            navArgument(NavArguments.MEDICATION_INFO.END_DATE) { type = NavType.StringType; defaultValue = "" },
                            navArgument(NavArguments.MEDICATION_INFO.DOSAGE) { type = NavType.StringType; defaultValue = "" }
                        )
                    ) { backStackEntry ->
                        MedicationInfoScreen(
                            medicationName = backStackEntry.arguments?.getString(NavArguments.MEDICATION_INFO.MEDICATION_NAME)?: "",
                            startDate = backStackEntry.arguments?.getString(NavArguments.MEDICATION_INFO.START_DATE)?: "",
                            endDate = backStackEntry.arguments?.getString(NavArguments.MEDICATION_INFO.END_DATE)?: "",
                            dosage = backStackEntry.arguments?.getString(NavArguments.MEDICATION_INFO.DOSAGE)?: ""
                        )
                    }

                    composable(
                        Routes.MEDICATION_EDIT_WITH_ARGS,
                        arguments = listOf(
                            navArgument(NavArguments.MEDICATION_EDIT.MEDICATION_ID) { type = NavType.StringType; defaultValue = "" },
                        )
                    ) {backStackEntry ->
                        MedicationEditScreen(
                            medicationId = backStackEntry.arguments?.getString(NavArguments.MEDICATION_EDIT.MEDICATION_ID)?: "",
                            patientId = backStackEntry.arguments?.getString(NavArguments.MEDICATION_LIST.PID)?: "",
                            onNavigateToPeopleList = { onNavigateToPeopleList() },
                            onNavigateToMedicationList = { pid: String -> onNavigateToMedicationList(pid) },
                        )
                    }


                    if (GlobalObjects.type == "patient") {
                        composable(Routes.HOME) { HomeScreen() }
                    }

                    if (GlobalObjects.type == "doctor") {
                        composable(Routes.ADD_PATIENT) { AddPatientScreen(onNavigateToMedicationList = { pid: String -> onNavigateToMedicationList(pid) },
                            { onNavigateToPeopleList() }) }
                    }
                }
            }
        }
    }
}

