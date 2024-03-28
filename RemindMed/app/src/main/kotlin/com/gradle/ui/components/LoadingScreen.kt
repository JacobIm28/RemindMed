package com.gradle.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.gradle.ui.theme.AppTheme
import com.example.remindmed.R
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
                .fillMaxHeight(),
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