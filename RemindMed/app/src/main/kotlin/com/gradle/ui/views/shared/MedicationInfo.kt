package com.gradle.ui.views.shared

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.gson.JsonObject
import com.gradle.apiCalls.MedicationApi
import com.gradle.constants.medicationInfoBlacklist
import com.gradle.ui.components.BoldText
import com.gradle.ui.components.HeadlineLarge
import com.gradle.ui.components.LoadingScreen
import com.gradle.ui.components.TitleLarge
import com.gradle.ui.components.ToggleList
import com.gradle.ui.theme.AppTheme
import com.gradle.utilities.formatJSONKey
import kotlinx.coroutines.delay

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MedicationInfoScreen(
    medicationName: String,
    startDate: String,
    endDate: String,
    dosage: String
) {
    var medicationInfo by remember { mutableStateOf<JsonObject?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(1)
        val result: JsonObject = MedicationApi().getMedicationbyName(medicationName)
        if (result.size() > 0) {
            medicationInfo = result["results"].asJsonArray[0].asJsonObject
        }
        isLoading = false
    }

    AppTheme {
        Scaffold { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
            ) {
                if (isLoading || medicationInfo == null) {
                    LoadingScreen()
                } else {
                    TitleLarge(text = medicationName)
                    Column {
                        HeadlineLarge(text = "Your prescription")
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, Color.LightGray, RoundedCornerShape(15.dp)),
                            colors = CardColors(
                                containerColor = MaterialTheme.colorScheme.tertiary,
                                contentColor = MaterialTheme.colorScheme.primary,
                                disabledContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                                disabledContentColor = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp)
                            ) {
                                Column(verticalArrangement = Arrangement.SpaceBetween) {
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        BoldText(text = "Dosage: ")
                                        Text(text = dosage)
                                    }
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        BoldText(text = "Start date: ")
                                        Text(text = startDate)
                                    }
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        BoldText(text = "End date: ")
                                        Text(text = endDate)
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.size(10.dp))

                    HeadlineLarge(text = "Information")

                    medicationInfo?.let { info ->
                        info.keySet()?.forEach {
                            if (!medicationInfoBlacklist.contains(it) && info[it].isJsonArray && info[it].asJsonArray.size() == 1) {
                                ToggleList(
                                    header = formatJSONKey(it),
                                    content = info[it].asString
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

