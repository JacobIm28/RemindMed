package com.gradle.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.remindmed.R
import com.gradle.ui.theme.AppTheme
import kotlinx.coroutines.delay

@Composable
fun LoadingScreen() {
    var rotationDegrees by remember { mutableStateOf(0f) }

    LaunchedEffect(key1 = rotationDegrees) {
        while (true) {
            rotationDegrees += 5f
            delay(100)
        }
    }

    AppTheme {
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(0.dp, 250.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                modifier = Modifier.rotate(rotationDegrees).size(100.dp),
                painter = painterResource(id = R.drawable.logotransparent),
                contentDescription = "Loading"
            )
            Spacer(modifier = Modifier.height(10.dp))

            Text("Loading")
        }

    }
}