package com.gradle.ui.views.shared

import TextInput
import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cs346.remindmed.R
import com.gradle.apiCalls.DoctorApi
import com.gradle.apiCalls.PatientApi
import com.gradle.constants.GlobalObjects
import com.gradle.models.Doctor
import com.gradle.models.Patient
import com.gradle.ui.components.HeadlineLarge
import com.gradle.ui.theme.AppTheme
import com.gradle.ui.theme.button_colour
import com.gradle.ui.theme.md_theme_light_onSecondary
import com.gradle.ui.theme.md_theme_light_primary
import com.gradle.ui.viewModels.LoginViewModel
import com.gradle.ui.views.RemindMedApp

@RequiresApi(Build.VERSION_CODES.S)
@SuppressLint("RememberReturnType", "UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Login(mainViewModel: LoginViewModel = viewModel()) {
    AppTheme {
        if (mainViewModel.userIsComplete) {
            RemindMedApp(mainViewModel)
        } else {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = androidx.compose.material.MaterialTheme.colors.background
            ) {
                MainView(mainViewModel)
            }
        }
    }
}

@Composable
fun MainView(
    viewModel: LoginViewModel
) {
    // Wake up the API
    LaunchedEffect(Unit) {
        PatientApi().getAllPatients()
    }

    Column(
        modifier = Modifier.padding(20.dp, 50.dp, 20.dp, 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = "RemindMed",
            style = TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Bold,
                fontSize = 60.sp,
                color = md_theme_light_primary
            )
        )

        if (viewModel.userIsAuthenticated && !viewModel.userIsComplete) {
            var name by rememberSaveable { mutableStateOf(viewModel.user.name) }
            var type by rememberSaveable { mutableStateOf(viewModel.user.type) }

            if (!validateName(name)) {
                Text(
                    text = "Please enter a valid name",
                    style = TextStyle(color = androidx.compose.material.MaterialTheme.colors.error)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
            ) {
                HeadlineLarge(
                    text = "Please enter your name and select your type"
                )
                Spacer(modifier = Modifier.height(20.dp))
                TextInput(
                    label = "Enter your name",
                    placeholder = "Name",
                    value = name,
                    onValueChange = { name = it }
                )
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column {
                        Text("Patient")
                        RadioButton(
                            selected = type == "patient",
                            onClick = { type = "patient" },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = button_colour
                            )
                        )
                    }
                    Column {
                        Text("Doctor")
                        RadioButton(
                            selected = type == "doctor",
                            onClick = { type = "doctor" },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = button_colour
                            )
                        )
                    }
                }
            }

            viewModel.user.name = name
            viewModel.user.type = type
        }

        var buttonText: String = "Begin"
        var onClickAction: () -> Unit = { viewModel.login() }
        if (viewModel.userIsAuthenticated && !viewModel.userIsComplete) {
            buttonText = "Proceed"
            onClickAction = {
                if (validateName(viewModel.user.name) && (viewModel.user.type == "patient" || viewModel.user.type == "doctor")) {
                    viewModel.userIsComplete = processNewUser(
                        viewModel.user.name,
                        viewModel.user.type,
                        viewModel.user.email,
                        viewModel.user.id
                    )
                    if (viewModel.user.type == "patient") {
                        GlobalObjects.patient =
                            Patient(viewModel.user.id, viewModel.user.name, viewModel.user.email)
                        GlobalObjects.type = "patient"
                    } else {
                        GlobalObjects.doctor =
                            Doctor(viewModel.user.id, viewModel.user.name, viewModel.user.email)
                        GlobalObjects.type = "doctor"
                    }
                }
                viewModel.isLoading = true
            }
        } else {
            Image(
                painter = painterResource(id = R.drawable.logotransparent),
                contentDescription = "RemindMed Logo",
                modifier = Modifier
                    .size(500.dp)
                    .padding(20.dp)
            )
        }
        LogButton(
            text = buttonText,
            onClick = onClickAction,
        )
    }
}

@Composable
fun LogButton(
    text: String,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(
            onClick = { onClick() },
            elevation = ButtonDefaults.elevation(defaultElevation = 0.dp, pressedElevation = 0.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .border(0.dp, md_theme_light_onSecondary),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = button_colour,
                contentColor = md_theme_light_onSecondary
            ),
            shape = RoundedCornerShape(150.dp)
        ) {
            Text(
                text = text,
                fontSize = 20.sp,
            )
        }
    }
}

fun processNewUser(name: String, type: String, email: String, id: String): Boolean {
    if (type == "patient" && name.isNotEmpty() && email.isNotEmpty() && id.isNotEmpty()) {
        PatientApi().addPatient(Patient(id, name, email))
        return true
    } else if (type == "doctor" && name.isNotEmpty() && email.isNotEmpty() && id.isNotEmpty()) {
        DoctorApi().addDoctor(Doctor(id, name, email))
        return true
    }

    return false
}

fun validateName(name: String): Boolean {
    return name.isNotEmpty() && name.length > 2 && name.matches(Regex(".*[a-zA-Z0-9].*"))
}

