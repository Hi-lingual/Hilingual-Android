package com.hilingual.core.designsystem.component.textfield

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.chattymin.pebble.graphemeLength
import com.hilingual.core.designsystem.theme.HilingualTheme

enum class TextFieldState {
    NORMAL, ERROR, SUCCESS
}

@Composable
fun HilingualShortTextField(
    value: () -> String,
    placeholder: String,
    onValueChanged: (String) -> Unit,
    maxLength: Int,
    state: () -> TextFieldState,
    errorMessage: () -> String,
    successMessage: String,
    modifier: Modifier = Modifier,
    height: Dp = 54.dp,
    onDoneAction: () -> Unit = {}
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val text = value()
    val currentState = state()

    val borderColor = when (currentState) {
        TextFieldState.NORMAL, TextFieldState.SUCCESS -> Color.Transparent
        TextFieldState.ERROR -> HilingualTheme.colors.alertRed
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        HilingualBasicTextField(
            value = text,
            placeholder = placeholder,
            maxLength = maxLength,
            onValueChanged = {
                val newText = it.filter { !it.isWhitespace() }
                if (newText.graphemeLength <= maxLength) {
                    onValueChanged(newText)
                }
            },
            modifier = modifier.height(height),
            borderColor = borderColor,
            onDoneAction = {
                onDoneAction()
                keyboardController?.hide()
            },
            verticalPadding = PaddingValues(vertical = 16.dp)
        )
        Row {
            Text(
                text = when (currentState) {
                    TextFieldState.ERROR -> errorMessage()
                    TextFieldState.SUCCESS -> successMessage
                    else -> ""
                },
                style = HilingualTheme.typography.captionR12,
                color = when (currentState) {
                    TextFieldState.ERROR -> HilingualTheme.colors.alertRed
                    TextFieldState.SUCCESS -> HilingualTheme.colors.hilingualBlue
                    else -> Color.Transparent
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "${text.graphemeLength} / $maxLength",
                style = HilingualTheme.typography.captionR12,
                color = HilingualTheme.colors.gray300
            )
        }
    }
}
