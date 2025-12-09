/*
 * Copyright 2025 The Hilingual Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hilingual.core.designsystem.component.textfield

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.chattymin.pebble.graphemeLength
import com.hilingual.core.designsystem.theme.HilingualTheme

private val INPUT_FILTER_REGEX = Regex("""[^\p{IsHangul}a-zA-Z0-9\p{Punct}\p{S}]""")

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
            placeholderTextStyle = HilingualTheme.typography.bodyR16,
            inputTextStyle = HilingualTheme.typography.bodyM16,
            onValueChanged = {
                val filteredText = it.replace(INPUT_FILTER_REGEX, "")
                val newText = filteredText.filter { !it.isWhitespace() }
                if (newText.graphemeLength <= maxLength) {
                    onValueChanged(newText)
                }
            },
            modifier = modifier,
            borderColor = borderColor,
            onDoneAction = {
                onDoneAction()
                keyboardController?.hide()
            },
            paddingValues = PaddingValues(vertical = 16.dp, horizontal = 12.dp)
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
