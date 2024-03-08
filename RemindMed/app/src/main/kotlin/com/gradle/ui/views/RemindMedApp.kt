package com.gradle.ui.views

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.gradle.constants.Routes
import com.gradle.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("RememberReturnType", "UnusedMaterialScaffoldPaddingParameter")
@Composable
fun RemindMedApp() {
    val navController = rememberNavController();
    AppTheme {
        // Routing
        Scaffold (
            bottomBar = {
                BottomNavigation (
                    backgroundColor = MaterialTheme.colorScheme.tertiary
                ) {

                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination

                    BottomNavigationItem(
                        icon = { Icon(Icons.Rounded.Home, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                        label = { Text("Home", style = MaterialTheme.typography.bodySmall) },
                        selected = currentDestination?.hierarchy?.any { it.route == Routes.HOME } == true,
                        onClick = {
                            navController.navigate(Routes.HOME) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                    BottomNavigationItem(
                        icon = { Icon(Icons.AutoMirrored.Rounded.List, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                        label = { Text("List", style = MaterialTheme.typography.bodySmall) },
                        selected = currentDestination?.hierarchy?.any { it.route == Routes.MEDICATION_LIST } == true,
                        onClick = {
                            navController.navigate(Routes.MEDICATION_LIST) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                    BottomNavigationItem(
                        icon = { Icon(Icons.Rounded.Person, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                        label = { Text("Profile", style = MaterialTheme.typography.bodySmall) },
                        selected = currentDestination?.hierarchy?.any { it.route == Routes.PROFILE } == true,
                        onClick = {
                            navController.navigate(Routes.PROFILE) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        ) { innerPadding ->
            Column (
                modifier = Modifier.padding(innerPadding).padding(25.dp)
            ) {
                NavHost(navController, startDestination = "home") {
                    composable(Routes.HOME) { HomeScreen(navController) }
                    composable(Routes.MEDICATION_LIST) { MedicationListScreen(navController) }
                    // composable(Routes.PROFILE) { ProfileScreen(navController) }
                    composable(Routes.PROFILE) { DoctorPatientListScreen(navController) }
                    composable(Routes.USER_MEDICATION_ENTRY) { UserMedicationEntryScreen(navController) }
                    composable(Routes.DOCTOR_VIEW_MEDICATION_LIST) {DoctorViewMedicationListScreen(navController)}
                }
            }
        }
    }
}