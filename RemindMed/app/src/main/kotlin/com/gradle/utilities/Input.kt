import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gradle.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
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
        Column {
            if (isError) {
                Text(errorMessage, color = MaterialTheme.colorScheme.error)
            } else {
                Text(label)
            }
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                colors = TextFieldDefaults.colors(
                    focusedTextColor = FilledTextFieldTokens.FocusInputColor.value,
                    unfocusedTextColor = FilledTextFieldTokens.InputColor.value,
                    disabledTextColor = FilledTextFieldTokens.DisabledInputColor.value
                        .copy(alpha = FilledTextFieldTokens.DisabledInputOpacity),
                    errorTextColor = FilledTextFieldTokens.ErrorInputColor.value,
                    focusedContainerColor = MaterialTheme.colorScheme.tertiary,
                    unfocusedContainerColor = MaterialTheme.colorScheme.tertiary,
                    disabledContainerColor = MaterialTheme.colorScheme.tertiary,
                    errorContainerColor = FilledTextFieldTokens.ContainerColor.value,
                    cursorColor = FilledTextFieldTokens.CaretColor.value,
                    errorCursorColor = FilledTextFieldTokens.ErrorFocusCaretColor.value,
                    selectionColors = LocalTextSelectionColors.current,
                    focusedIndicatorColor = FilledTextFieldTokens.FocusActiveIndicatorColor.value,
                    unfocusedIndicatorColor = FilledTextFieldTokens.ActiveIndicatorColor.value,
                    disabledIndicatorColor = FilledTextFieldTokens.DisabledActiveIndicatorColor.value
                        .copy(alpha = FilledTextFieldTokens.DisabledActiveIndicatorOpacity),
                    errorIndicatorColor = FilledTextFieldTokens.ErrorActiveIndicatorColor.value,
                    focusedLeadingIconColor = FilledTextFieldTokens.FocusLeadingIconColor.value,
                    unfocusedLeadingIconColor = FilledTextFieldTokens.LeadingIconColor.value,
                    disabledLeadingIconColor = FilledTextFieldTokens.DisabledLeadingIconColor.value
                        .copy(alpha = FilledTextFieldTokens.DisabledLeadingIconOpacity),
                    errorLeadingIconColor = FilledTextFieldTokens.ErrorLeadingIconColor.value,
                    focusedTrailingIconColor = FilledTextFieldTokens.FocusTrailingIconColor.value,
                    unfocusedTrailingIconColor = FilledTextFieldTokens.TrailingIconColor.value,
                    disabledTrailingIconColor = FilledTextFieldTokens.DisabledTrailingIconColor.value
                        .copy(alpha = FilledTextFieldTokens.DisabledTrailingIconOpacity),
                    errorTrailingIconColor = FilledTextFieldTokens.ErrorTrailingIconColor.value,
                    focusedLabelColor = FilledTextFieldTokens.FocusLabelColor.value,
                    unfocusedLabelColor = FilledTextFieldTokens.LabelColor.value,
                    disabledLabelColor = FilledTextFieldTokens.DisabledLabelColor.value
                        .copy(alpha = FilledTextFieldTokens.DisabledLabelOpacity),
                    errorLabelColor = FilledTextFieldTokens.ErrorLabelColor.value,
                    focusedPlaceholderColor = FilledTextFieldTokens.InputPlaceholderColor.value,
                    unfocusedPlaceholderColor = FilledTextFieldTokens.InputPlaceholderColor.value,
                    disabledPlaceholderColor = FilledTextFieldTokens.DisabledInputColor.value
                        .copy(alpha = FilledTextFieldTokens.DisabledInputOpacity),
                    errorPlaceholderColor = FilledTextFieldTokens.InputPlaceholderColor.value,
                    focusedSupportingTextColor = FilledTextFieldTokens.FocusSupportingColor.value,
                    unfocusedSupportingTextColor = FilledTextFieldTokens.SupportingColor.value,
                    disabledSupportingTextColor = FilledTextFieldTokens.DisabledSupportingColor.value
                        .copy(alpha = FilledTextFieldTokens.DisabledSupportingOpacity),
                    errorSupportingTextColor = FilledTextFieldTokens.ErrorSupportingColor.value,
                    focusedPrefixColor = FilledTextFieldTokens.InputPrefixColor.value,
                    unfocusedPrefixColor = FilledTextFieldTokens.InputPrefixColor.value,
                    disabledPrefixColor = FilledTextFieldTokens.InputPrefixColor.value
                        .copy(alpha = FilledTextFieldTokens.DisabledInputOpacity),
                    errorPrefixColor = FilledTextFieldTokens.InputPrefixColor.value,
                    focusedSuffixColor = FilledTextFieldTokens.InputSuffixColor.value,
                    unfocusedSuffixColor = FilledTextFieldTokens.InputSuffixColor.value,
                    disabledSuffixColor = FilledTextFieldTokens.InputSuffixColor.value
                        .copy(alpha = FilledTextFieldTokens.DisabledInputOpacity),
                    errorSuffixColor = FilledTextFieldTokens.InputSuffixColor.value,
                ),
                value = value,
                onValueChange = onValueChange,
                maxLines = 1,
                isError = isError,
                trailingIcon = {
                    if (isError) Icon(
                        Icons.Filled.Info,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
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
                Text(errorMessage, color = MaterialTheme.colorScheme.tertiary)
            } else {
                Text(label, color = MaterialTheme.colorScheme.tertiary)
            }
            Spacer(modifier = Modifier.height(5.dp))
            TextField(
                value = value,
                onValueChange = onValueChange,
                label = { Text(placeholder) },
                maxLines = lines,
                isError = isError,
                trailingIcon = {
                    if (isError) Icon(
                        Icons.Filled.Info,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            )
        }
    }
}
