package com.gradle.ui.views.shared

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
import androidx.compose.material.RadioButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gradle.constants.GlobalObjects
import com.gradle.models.Doctor
import com.gradle.models.LoginModel
import com.gradle.models.Patient
import com.gradle.ui.components.*
import com.gradle.ui.views.RemindMedApp
import com.gradle.apiCalls.Patient as PatientApi
import com.gradle.apiCalls.Doctor as DoctorApi

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
                .width(200.dp)
                .height(50.dp),
        ) {
            Text(
                text = text,
                fontSize = 20.sp,
            )
        }
    }
}
@Composable
fun Title(
    text: String,
)
{
    Text(
        text = text,
        style = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
        )
    )
}

fun processNewUser(name: String, type: String, email: String, id: String): Boolean {
    println(type)
    println(name)
    println(email)
    println(id)
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
    Column(
        modifier = Modifier.padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        // Title
        // -----
        val title = if (viewModel.userIsAuthenticated) {
            stringResource(R.string.logged_in_title)
        } else {
            if (viewModel.appJustLaunched) {
                stringResource(R.string.initial_title)
            } else {
                stringResource(R.string.logged_out_title)
            }
        }
        Title(
            text = title
        )

        if (viewModel.userIsAuthenticated && !viewModel.userIsComplete) {
            var name by rememberSaveable { mutableStateOf(viewModel.user.name) }
            var type by rememberSaveable { mutableStateOf(viewModel.user.type) }

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
                Text("Patient")
                RadioButton(
                    selected = type == "patient",
                    onClick = { type = "patient" }
                )
                Text("Doctor")
                RadioButton(
                    selected = type == "doctor",
                    onClick = { type = "doctor" }
                )
            }

            viewModel.user.name= name
            viewModel.user.type= type
        }

        val buttonText: String
        val onClickAction: () -> Unit
        if (viewModel.userIsAuthenticated) {
            buttonText = "Proceed"
            onClickAction = { if(viewModel.user.name.isNotEmpty() && (viewModel.user.type == "patient" || viewModel.user.type == "doctor")) {
                viewModel.userIsComplete = processNewUser(viewModel.user.name, viewModel.user.type, viewModel.user.email, viewModel.user.id)
                if (viewModel.user.type == "patient") {
                    GlobalObjects.patient = Patient(viewModel.user.id, viewModel.user.name, viewModel.user.email)
                    GlobalObjects.type = "patient"
                } else {
                    GlobalObjects.doctor = Doctor(viewModel.user.id, viewModel.user.name, viewModel.user.email)
                    GlobalObjects.type = "doctor"
                }
            } }
        } else {
            buttonText = stringResource(R.string.log_in_button)
            onClickAction = { viewModel.login() }
        }
        LogButton(
            text = buttonText,
            onClick = onClickAction,
        )
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