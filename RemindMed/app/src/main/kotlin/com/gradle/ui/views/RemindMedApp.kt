package com.gradle.ui.views

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
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
import com.gradle.models.Medication
import com.gradle.ui.components.ButtonPrimary
import com.gradle.ui.views.doctor.AddPatientScreen
import com.gradle.ui.views.patient.HomeScreen
import com.gradle.ui.views.shared.MedicationEntryScreen
import com.gradle.ui.views.shared.MedicationInfoScreen
import com.gradle.ui.views.shared.MedicationListScreen
import com.gradle.ui.views.shared.PeopleListScreen
import com.gradle.ui.views.shared.ProfileScreen
import com.gradle.utilities.notifications.NotificationUtils
import java.sql.Date

data class NavigationItem(
    val icon: ImageVector,
    val label: String,
    val route: String
)

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("RememberReturnType", "UnusedMaterialScaffoldPaddingParameter")
@Composable
fun RemindMedApp(context: Context) {
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
            icon = Icons.Rounded.AccountCircle,
            label = "Profile",
            route = Routes.PROFILE
        )
    )

    val navBarItems = if (GlobalObjects.type == "doctor") doctorNavBarItems else patientNavBarItems

    fun onNavigateToMedicationInfo(medicationId: String) {
        navController.navigate(Routes.MEDICATION_INFO)
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
                            label = { Text(navItem.label, style = MaterialTheme.typography.bodySmall) },
                            selected = currentDestination?.hierarchy?.any { it.route == navItem.route} == true,
                            onClick = {
                                navController.navigate(navItem.route) {
//                                    popUpTo(navController.graph.findStartDestination().id) {
//                                        saveState = true
//                                    }
//                                    launchSingleTop = true
//                                    restoreState = true
                                }
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
                // TODO: Remove unused routes from the navhosts
                ButtonPrimary(text = "asd", onClick = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        NotificationUtils.scheduleNotifications(
                            context,
                            GlobalObjects.patient,
                            Medication("0", "asdf8s7fas7d8f", "2mg", Date(0), Date(0), "Advil", "Eat with food", mutableListOf()))
                    }
                },
                    enabled = true)
                
                NavHost(navController, startDestination = if (GlobalObjects.type == "doctor") Routes.PEOPLE_LIST else Routes.HOME ) {
                    composable(Routes.PEOPLE_LIST) { PeopleListScreen(navController) }
                

                    composable(
                        Routes.MEDICATION_LIST_WITH_ARGS,
                        arguments = listOf(navArgument(NavArguments.MEDICATION_LIST.PID) { type = NavType.StringType })
                    ) {backStackEntry ->
                        MedicationListScreen(
                            navController,
                            pid = backStackEntry.arguments?.getString(NavArguments.MEDICATION_LIST.PID)?: ""
                        )
                    }

                    composable(Routes.PROFILE) { ProfileScreen(navController) }

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
                            navController,
                            medicationName = backStackEntry.arguments?.getString(NavArguments.MEDICATION_INFO.MEDICATION_NAME)?: "",
                            startDate = backStackEntry.arguments?.getString(NavArguments.MEDICATION_INFO.START_DATE)?: "",
                            endDate = backStackEntry.arguments?.getString(NavArguments.MEDICATION_INFO.END_DATE)?: "",
                            dosage = backStackEntry.arguments?.getString(NavArguments.MEDICATION_INFO.DOSAGE)?: ""
                        )
                    }

                    composable(Routes.MEDICATION_ENTRY) { MedicationEntryScreen(navController) }

                    if (GlobalObjects.type == "patient") {
                        composable(Routes.HOME) { HomeScreen(navController) }
                    }

                    if (GlobalObjects.type == "doctor") {
                        composable(Routes.ADD_PATIENT) { AddPatientScreen(navController) }
                    }
                }
            }
        }
    }
}

