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

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.common.util.EmojiFilter.removeEmoji
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
fun HilingualLongTextField(
    value: String,
    onValueChanged: (String) -> Unit,
    maxLength: Int,
    modifier: Modifier = Modifier,
    placeholder: String = "What's been going on today?",
    onDoneAction: () -> Unit = {}
) {
    var isFocused by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    HilingualBasicTextField(
        value = value,
        maxLength = maxLength,
        placeholder = placeholder,
        placeholderTextStyle = HilingualTheme.typography.bodyR15,
        inputTextStyle = HilingualTheme.typography.bodyR15,
        decorationBoxHeight = 241.dp,
        onValueChanged = {
            val filteredValue = it.removeEmoji()
            if (filteredValue.length <= maxLength) onValueChanged(filteredValue)
        },
        onFocusChanged = { isFocused = it },
        modifier = modifier,
        borderColor = if (isFocused) HilingualTheme.colors.black else Color.Unspecified,
        singleLine = false,
        onDoneAction = {
            onDoneAction()
            keyboardController?.hide()
        },
        isShowLength = true,
        keyboardImeAction = ImeAction.Default
    )
}

@Preview
@Composable
private fun HilingualLongTextFieldPreview() {
    var text by remember { mutableStateOf("") }
    val context = LocalContext.current

    HilingualTheme {
        HilingualLongTextField(
            value = text,
            onValueChanged = { text = it },
            onDoneAction = {
                Toast.makeText(context, "Enter key pressed with text: $text", Toast.LENGTH_SHORT)
                    .show()
            },
            maxLength = 1000
        )
    }
}
