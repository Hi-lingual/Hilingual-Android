package com.hilingual.core.designsystem.component.textfield

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
fun HilingualLongTextField(
    value: String,
    onValueChanged: (String) -> Unit,
    maxLength: Int,
    modifier: Modifier = Modifier,
    height: Dp = 292.dp,
    placeholder: String = "What's been going on today?",
    onDoneAction: () -> Unit = {},
) {
    var isFocused by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    HilingualBasicTextField(
        value = value,
        placeholder = placeholder,
        textStyle = HilingualTheme.typography.bodyM16,
        onValueChanged = {
            if (it.length <= maxLength) onValueChanged(it)
        },
        onFocusChanged = { isFocused = it },
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .height(height),
        borderModifier = if (isFocused) {
            Modifier.border(
                1.dp,
                HilingualTheme.colors.black,
                RoundedCornerShape(8.dp)
            )
        } else {
            Modifier
        },
        singleLine = false,
        onDoneAction = {
            onDoneAction()
            keyboardController?.hide()
        },
        bottomRightContent = {
            Text(
                text = "${value.length} / $maxLength",
                style = HilingualTheme.typography.captionR12,
                color = HilingualTheme.colors.gray400
            )
        }
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
            maxLength = 100
        )
    }
}