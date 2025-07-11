package com.hilingual.core.designsystem.component.textfield

import android.widget.Toast
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.designsystem.R
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
fun HilingualSearchTextField(
    value: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    height: Dp = 46.dp,
    placeholder: String = "단어나 표현을 검색해 주세요",
    onSearchAction: () -> Unit = {},
    onTrailingIconClick: () -> Unit = {}
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    HilingualBasicTextField(
        value = value,
        placeholder = placeholder,
        textStyle = HilingualTheme.typography.bodyM16,
        onValueChanged = onValueChanged,
        modifier = modifier.height(height),
        backgroundColor = HilingualTheme.colors.white,
        leadingIcon = {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_search_20),
                contentDescription = null,
                tint = HilingualTheme.colors.gray500,
                modifier = Modifier
                    .padding(end = 4.dp)
                    .size(20.dp)
            )
        },
        trailingIcon = {
            if (value.isNotEmpty()) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_cancel_20),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .size(20.dp)
                        .noRippleClickable(
                            onClick = onTrailingIconClick
                        )
                )
            }
        },
        keyboardImeAction = ImeAction.Search,
        onSearchAction = {
            onSearchAction()
            keyboardController?.hide()
        }
    )
}

@Preview
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