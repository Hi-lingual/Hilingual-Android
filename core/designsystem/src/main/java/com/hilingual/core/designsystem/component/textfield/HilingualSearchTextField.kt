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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.designsystem.R
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
fun HilingualSearchTextField(
    value: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "단어나 표현을 검색해 주세요",
    backgroundColor: Color = HilingualTheme.colors.white,
    onSearchAction: () -> Unit = {},
    onTrailingIconClick: () -> Unit = {},
    paddingValues: PaddingValues = PaddingValues(12.dp)
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    HilingualBasicTextField(
        value = value,
        placeholder = placeholder,
        onValueChanged = onValueChanged,
        modifier = modifier,
        placeholderTextStyle = HilingualTheme.typography.bodyM16,
        inputTextStyle = HilingualTheme.typography.bodyM16,
        backgroundColor = backgroundColor,
        leadingIcon = {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_search_20),
                contentDescription = null,
                tint = HilingualTheme.colors.gray500,
                modifier = Modifier.size(20.dp)
            )
        },
        trailingIcon = {
            if (value.isNotEmpty()) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_close_circle_20),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .size(20.dp)
                        .noRippleClickable(onClick = onTrailingIconClick)
                )
            }
        },
        keyboardType = KeyboardType.Email,
        keyboardImeAction = ImeAction.Search,
        onSearchAction = {
            onSearchAction()
            keyboardController?.hide()
        },
        hintLocales = LocaleList(Locale("en")),
        paddingValues = paddingValues
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF212121)
@Composable
private fun HilingualSearchTextFieldPreview() {
    var text by remember { mutableStateOf("") }
    val context = LocalContext.current

    HilingualTheme {
        HilingualSearchTextField(
            value = text,
            onValueChanged = { text = it },
            onSearchAction = {
                Toast.makeText(context, "Enter key pressed with text: $text", Toast.LENGTH_SHORT)
                    .show()
            }
        )
    }
}
