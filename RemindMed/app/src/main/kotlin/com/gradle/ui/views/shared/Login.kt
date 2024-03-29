package com.gradle.ui.views.shared

import TextInput
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import com.example.remindmed.R
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.gradle.ui.theme.*
import androidx.compose.ui.Alignment
import androidx.compose.material.Button
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gradle.constants.GlobalObjects
import com.gradle.models.Doctor
import com.gradle.models.LoginModel
import com.gradle.models.Patient
import com.gradle.ui.components.*
import com.gradle.ui.views.RemindMedApp
import com.gradle.apiCalls.Patient as PatientApi
import com.gradle.apiCalls.Doctor as DoctorApi
import com.gradle.ui.components.LoadingScreen
fun validateName(name: String): Boolean {
    return name.isNotEmpty() && name.length > 2 && name.matches(Regex(".*[a-zA-Z0-9].*"))
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
    println(name)
    println(type)
    println(email)
    if(type == "patient" && name.isNotEmpty() && email.isNotEmpty() && id.isNotEmpty()) {
        PatientApi().addPatient(Patient(id, name, email))
        return true
    } else if (type == "doctor" && name.isNotEmpty() && email.isNotEmpty() && id.isNotEmpty()) {
        DoctorApi().addDoctor(Doctor(id, name, email))
        return true
    } else {
        println("Invalid user type")
    }

    return false
}
@Composable
fun MainView(
    viewModel: LoginModel
) {
    // Wake up the API
    //TODO: temporary fix, need to wake up API in a better way or at a diff spot.
    LaunchedEffect(Unit) {
        PatientApi().getAllPatients()
    }

    //TODO: Uncomment to skip login process

//    GlobalObjects.type = "patient"
//    GlobalObjects.patient = PatientApi().getPatientbyId("65f9aa62cd606f2e1413f38e")
//    viewModel.userIsAuthenticated = true
//    viewModel.userIsComplete = true

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

            if(!validateName(name)) {
                Text(text = "Please enter a valid name", style = TextStyle(color = MaterialTheme.colors.error))
            }

           Column (
                modifier = Modifier.fillMaxWidth().padding(20.dp),
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
            println(viewModel.user.name)
            println(viewModel.user.type)
            println(name)
        }

        var buttonText: String = "Begin"
        var onClickAction: () -> Unit = { viewModel.login()}
        if (viewModel.userIsAuthenticated && !viewModel.userIsComplete) {
            buttonText = "Proceed"
            onClickAction = { if(validateName(viewModel.user.name) && (viewModel.user.type == "patient" || viewModel.user.type == "doctor")) {
                viewModel.userIsComplete = processNewUser(viewModel.user.name, viewModel.user.type, viewModel.user.email, viewModel.user.id)
                if (viewModel.user.type == "patient") {
                    GlobalObjects.patient = Patient(viewModel.user.id, viewModel.user.name, viewModel.user.email)
                    GlobalObjects.type = "patient"
                } else {
                    GlobalObjects.doctor = Doctor(viewModel.user.id, viewModel.user.name, viewModel.user.email)
                    GlobalObjects.type = "doctor"
                }
            }
            viewModel.isLoading = true}
        } else {
            Image (
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

        println(viewModel.isLoading)

        if(viewModel.isLoading) {
            LoadingScreen()
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("RememberReturnType", "UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Login(mainViewModel: LoginModel = viewModel(), context: Context) {
    AppTheme {
        if(mainViewModel.userIsComplete){
            RemindMedApp(context)
        } else {
            println("Reached here")
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colors.background
            ) {
                MainView(mainViewModel)
            }
        }
    }
}
