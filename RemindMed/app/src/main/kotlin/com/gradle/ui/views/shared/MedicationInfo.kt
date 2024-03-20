package com.gradle.ui.views.shared

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
//import androidx.compose.material3
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.remindmed.R
import com.google.gson.JsonObject
import com.gradle.constants.GlobalObjects
import com.gradle.constants.Routes
import com.gradle.models.Medication
import com.gradle.ui.components.BoldText
import com.gradle.ui.components.HeadLineMedium
import com.gradle.ui.components.TitleLarge
import com.gradle.ui.theme.AppTheme
import com.gradle.ui.theme.container_colour
import org.json.JSONObject
import java.sql.Date
import com.gradle.apiCalls.Patient as PatientApi
import com.gradle.apiCalls.Medication as MedicationApi

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MedicationInfoScreen(navController: NavController, medicationName: String, startDate: String, endDate: String, dosage: String) {
    // TODO: Get medication as a prop passed in from medication card
    var medicationInfo: JsonObject = JsonObject()

    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        println("Fetching Medication Info for " + medicationName)
        medicationInfo = MedicationApi().getMedicationbyName(medicationName)
        isLoading = false
        println(medicationInfo)
    }

    AppTheme {
        Scaffold { padding ->
            Column (modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())) {
                if (isLoading) {
                    Text("Loading...")
                } else {
                    TitleLarge(text = medicationName)
                    Column() {
                        HeadLineMedium(text = "Your prescription")
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.medicine),
                                contentDescription = "",
                                modifier = Modifier
                                    .fillMaxWidth(0.3f)
                                    .size(90.dp)
                            )
                            Spacer(modifier = Modifier.fillMaxWidth(0.1f))
                            Column(verticalArrangement = Arrangement.SpaceBetween) {
                                Row (horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                    BoldText(text = "Dosage: ")
                                    Text(text = dosage)
                                }
                                Row (horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                    BoldText(text = "Start date: ")
                                    Text(text = startDate)
                                }
                                Row (horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                    BoldText(text = "End date: ")
                                    Text(text = endDate)
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.size(10.dp))
                    Column() {
                        HeadLineMedium(text = "Purpose")
                        Text(text = medicationInfo["results"].asJsonArray[0].asJsonObject["purpose"].asString)
                    }
                    Spacer(modifier = Modifier.size(10.dp))
                    Column() {
                        HeadLineMedium(text = "Indications and Usage")
                        Text(text = medicationInfo["results"].asJsonArray[0].asJsonObject["indications_and_usage"].asString)
                    }
                    Spacer(modifier = Modifier.size(10.dp))
                    Column() {
                        HeadLineMedium(text = "Side Effects")
                        Text(text = medicationInfo["results"].asJsonArray[0].asJsonObject["warnings"].asString)
                    }
                    Spacer(modifier = Modifier.size(10.dp))
                    Column() {
                        HeadLineMedium(text = "Warnings")
                        Text(text = medicationInfo["results"].asJsonArray[0].asJsonObject["do_not_use"].asString)
                        Text(text = medicationInfo["results"].asJsonArray[0].asJsonObject["ask_doctor"].asString)
                        Text(text = medicationInfo["results"].asJsonArray[0].asJsonObject["ask_doctor_or_pharmacist"].asString)
                    }
                    Spacer(modifier = Modifier.size(10.dp))
                    Column() {
                        HeadLineMedium(text = "Dosage and Administration")
                        Text(text = medicationInfo["results"].asJsonArray[0].asJsonObject["dosage_and_administration"].asString)
                    }
                    Spacer(modifier = Modifier.size(10.dp))
                    Column() {
                        HeadLineMedium(text = "Storage and Handling")
                        Text(text = medicationInfo["results"].asJsonArray[0].asJsonObject["storage_and_handling"].asString)
                    }
                }
            }
        }
    }
}