package com.hilingual.core.designsystem.component.textfield

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.chattymin.pebble.graphemeLength
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
fun HilingualShortTextField(
    value: String,
    placeholder: String,
    onValueChanged: (String) -> Unit,
    maxLength: Int,
    isValid: () -> Boolean,
    errorMessage: String,
    successMessage: String,
    modifier: Modifier = Modifier,
    height: Dp = 54.dp,
    onDoneAction: () -> Unit = {},
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val isError = value.isNotEmpty() && !isValid()
    val isSuccess = value.isNotEmpty() && isValid()

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        HilingualBasicTextField(
            value = value,
            placeholder = placeholder,
            textStyle = HilingualTheme.typography.bodyM16,
            onValueChanged = {
                if (it.graphemeLength <= maxLength) onValueChanged(it)
            },
            modifier = modifier.height(height),
            borderModifier = Modifier.border(
                width = 1.dp,
                color = if (isError) HilingualTheme.colors.alertRed else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            ),
            onDoneAction = {
                onDoneAction()
                keyboardController?.hide()
            },
        )
        Row {
            Text(
                text = when {
                    isError -> errorMessage
                    isSuccess -> successMessage
                    else -> ""
                },
                style = HilingualTheme.typography.captionR12,
                color = when {
                    isError -> HilingualTheme.colors.alertRed
                    isSuccess -> HilingualTheme.colors.hilingualBlue
                    else -> Color.Transparent
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "${value.graphemeLength} / $maxLength",
                style = HilingualTheme.typography.captionR12,
                color = HilingualTheme.colors.gray300
            )
        }
    }
}

@Preview
@Composable
private fun HilingualShortTextFieldPreview() {
    var text by remember { mutableStateOf("") }
    val context = LocalContext.current

    HilingualTheme {
        HilingualShortTextField(
            value = text,
            placeholder = "한글, 영문, 숫자 조합만 가능",
            onValueChanged = { text = it },
            onDoneAction = {
                Toast.makeText(context, "Enter key pressed with text: $text", Toast.LENGTH_SHORT)
                    .show()
            },
            maxLength = 10,
            isValid = { text.matches(Regex("^[가-힣a-zA-Z0-9]+$")) },
            errorMessage = "한글, 영문, 숫자만 입력 가능합니다.",
            successMessage = "올바른 형식입니다."
        )
    }
}