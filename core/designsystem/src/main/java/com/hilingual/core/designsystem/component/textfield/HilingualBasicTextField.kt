package com.hilingual.core.designsystem.component.textfield

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
fun HilingualBasicTextField(
    value: String,
    onValueChanged: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    placeholderTextStyle: TextStyle = HilingualTheme.typography.bodyM16,
    inputTextStyle: TextStyle = HilingualTheme.typography.bodyM16,
    singleLine: Boolean = true,
    maxLength: Int = Int.MAX_VALUE,
    isShowLength: Boolean = false,
    keyboardImeAction: ImeAction = ImeAction.Done,
    keyboardType: KeyboardType = KeyboardType.Unspecified,
    onDoneAction: () -> Unit = {},
    onSearchAction: () -> Unit = {},
    focusRequester: FocusRequester = FocusRequester(),
    onFocusChanged: (Boolean) -> Unit = {},
    backgroundColor: Color = HilingualTheme.colors.gray100,
    borderColor: Color = Color.Unspecified,
    verticalPadding: PaddingValues = PaddingValues(vertical = 12.dp),
    decorationBoxHeight: Dp = 22.dp,
    leadingIcon: @Composable () -> Unit = {},
    trailingIcon: @Composable () -> Unit = {}
) {
    BasicTextField(
        value = value,
        onValueChange = {
            if (it.length <= maxLength) {
                onValueChanged(it)
            }
        },
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 12.dp)
            .focusRequester(focusRequester)
            .onFocusChanged { focusState ->
                onFocusChanged(focusState.isFocused)
            },
        singleLine = singleLine,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = keyboardImeAction
        ),
        keyboardActions = KeyboardActions(
            onDone = { onDoneAction() },
            onSearch = { onSearchAction() }
        ),
        textStyle = inputTextStyle.copy(
            color = HilingualTheme.colors.black
        ),
        decorationBox = { innerTextField ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                leadingIcon()

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(verticalPadding)
                ) {
                    Column(
                        modifier = Modifier.height(decorationBoxHeight)
                    ) {
                        Box {
                            if (value.isEmpty()) {
                                Text(
                                    text = placeholder,
                                    color = HilingualTheme.colors.gray400,
                                    style = placeholderTextStyle
                                )
                            }
                            innerTextField()
                        }
                    }

                    if (isShowLength) {
                        Spacer(Modifier.height(12.dp))
                        Text(
                            text = "${value.length} / $maxLength",
                            style = HilingualTheme.typography.captionR12,
                            color = HilingualTheme.colors.gray400,
                            modifier = Modifier.align(Alignment.End)
                        )
                    }
                }
                trailingIcon()
            }
        }
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF212121)
@Composable
private fun HilingualBasicTextFieldWithIconsPreview() {
    var text by remember { mutableStateOf("") }
    HilingualTheme {
        HilingualBasicTextField(
            value = text,
            onValueChanged = { text = it },
            placeholder = "Search",
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = HilingualTheme.colors.gray400
                )
            },
            trailingIcon = {
                if (text.isNotEmpty()) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear",
                        modifier = Modifier.clickable { text = "" }
                    )
                }
            }
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF212121)
@Composable
private fun HilingualBasicTextFieldPadding16Preview() {
    var text by remember { mutableStateOf("Input text") }
    HilingualTheme {
        HilingualBasicTextField(
            value = text,
            onValueChanged = { text = it },
            placeholder = "Enter text",
            verticalPadding = PaddingValues(vertical = 16.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF212121)
@Composable
private fun HilingualBasicTextFieldMultiLineWithLengthPreview() {
    var text by remember {
        mutableStateOf(
            "This is a long text that will wrap into multiple lines to demonstrate the behavior of the text field."
        )
    }
    HilingualTheme {
        HilingualBasicTextField(
            value = text,
            onValueChanged = { text = it },
            placeholder = "Enter your story",
            singleLine = false,
            decorationBoxHeight = 240.dp,
            isShowLength = true,
            maxLength = 1000
        )
    }
}
