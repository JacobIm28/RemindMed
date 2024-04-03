import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.gradle.apiCalls.MedicationApi
import com.gradle.ui.components.ButtonSecondary
import com.gradle.ui.components.HeadLineMedium
import com.gradle.ui.theme.AppTheme

@Composable
fun TextInput(
    label: String,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean = false,
    errorMessage: String = "Error"
) {
    AppTheme {
        Column(modifier = Modifier.padding(5.dp)) {
            if (isError) {
                Text(errorMessage, color = MaterialTheme.colorScheme.error)
            } else {
                Text(label)
            }
            Spacer(modifier = Modifier.height(5.dp))
            TextField(
                value = value,
                onValueChange = onValueChange,
                // label = { Text(placeholder) },
                maxLines = 1,
                isError = isError,
                trailingIcon = {
                    if (isError) Icon(Icons.Filled.Info, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun MultilineTextInput(
    label: String,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    lines: Int,
    isError: Boolean = false,
    errorMessage: String = "Error"
) {
    AppTheme {
        Column(modifier = Modifier.padding(5.dp)) {
            if (isError) {
                Text(errorMessage, color = MaterialTheme.colorScheme.error)
            } else {
                Text(label)
            }
            Spacer(modifier = Modifier.height(5.dp))
            TextField(
                value = value,
                onValueChange = onValueChange,
                label = { Text(placeholder) },
                maxLines = lines,
                isError = isError,
                trailingIcon = {
                    if (isError) Icon(Icons.Filled.Info, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                }
            )
        }
    }
}

@Composable
fun MedicationInput(
    onItemSelected: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }
    var isDropdownExpanded by remember { mutableStateOf(true) }
    var items by remember { mutableStateOf(mutableListOf<String>())   }
    var buttonClicked by remember{mutableStateOf(false)}

    Column {
        TextField(
            value = text,
            onValueChange = {
                text = it
                items = MedicationApi().getAllMedicationsbyName(it)
                buttonClicked = false
            },
            modifier = Modifier
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            singleLine = true,
            label = { Text("Type here") }
        )

        Row (modifier = Modifier.align(Alignment.CenterHorizontally)){
            ButtonSecondary(
                "Search",
                {
                    items = MedicationApi().getAllMedicationsbyName(text)
                    buttonClicked = true
                },
                true)
        }

        if (items.isEmpty() && buttonClicked) {
            HeadLineMedium(text = "Unfortunately No Matches")
        } else if (buttonClicked){
            DropdownMenu(
                expanded = isDropdownExpanded,
                onDismissRequest = {
                    // isDropdownExpanded = false
                    buttonClicked = false
                }
            ) {
                items.forEach { item ->
                    DropdownMenuItem(onClick = {
                        onItemSelected(item)
                        text = item
                        // isDropdownExpanded = false
                        buttonClicked = false
                    }) {
                        Text(text = item)
                    }
                }
            }
        }
    }
}