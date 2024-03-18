package com.gradle.ui.views

import android.annotation.SuppressLint
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.*
import com.gradle.constants.Routes
import com.gradle.ui.theme.AppTheme
import com.gradle.constants.*
import com.gradle.ui.views.doctor.AddPatientScreen
import com.gradle.ui.views.patient.HomeScreen
import com.gradle.ui.views.shared.MedicationInfoScreen
import com.gradle.ui.views.shared.MedicationListScreen
import com.gradle.ui.views.shared.PeopleListScreen
import com.gradle.ui.views.shared.ProfileScreen
import com.gradle.ui.views.shared.UserMedicationEntryScreen

data class NavigationItem(
    val icon: ImageVector,
    val label: String,
    val route: String
)
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("RememberReturnType", "UnusedMaterialScaffoldPaddingParameter")
@Composable
fun RemindMedApp() {
    val navController = rememberNavController();

    val patientNavBarItems = arrayOf(
        NavigationItem(
            icon = Icons.Rounded.Home,
            label = "Home",
            route = Routes.HOME
        ),
        NavigationItem(
            icon = Icons.Rounded.List,
            label = "Medications",
            route = Routes.MEDICATION_LIST
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

    val navBarItems = if (doctorView) doctorNavBarItems else patientNavBarItems

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
                if (doctorView) {
                    NavHost(navController, startDestination = Routes.PEOPLE_LIST) {
                        composable(Routes.PEOPLE_LIST) { PeopleListScreen(navController) }
                        composable(Routes.MEDICATION_LIST) { MedicationListScreen(navController) }
                        composable(Routes.PROFILE) { ProfileScreen(navController) }
                        composable(Routes.MEDICATION_ENTRY) { UserMedicationEntryScreen(navController) }
                        composable(Routes.MEDICATION_INFO) { MedicationInfoScreen(navController) }
                        composable(Routes.ADD_PATIENT) { AddPatientScreen(navController) }
                    }
                } else {
                    NavHost(navController, startDestination = Routes.HOME) {
                        composable(Routes.PEOPLE_LIST) { PeopleListScreen(navController) }
                        composable(Routes.HOME) { HomeScreen(navController) }
                        composable(Routes.MEDICATION_LIST) { MedicationListScreen(navController) }
                        composable(Routes.PROFILE) { ProfileScreen(navController) }
                        composable(Routes.MEDICATION_ENTRY) { UserMedicationEntryScreen(navController) }
                        composable(Routes.MEDICATION_INFO) { MedicationInfoScreen(navController) }
                    }
                }
            }
        }
    }
}