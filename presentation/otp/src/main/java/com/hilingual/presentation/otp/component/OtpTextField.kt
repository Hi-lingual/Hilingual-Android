package com.hilingual.presentation.otp.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.core.designsystem.theme.white

@Composable
fun OtpTextField(
    otpText: String,
    onOtpTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false
) {
    BasicTextField(
        modifier = modifier,
        value = otpText,
        onValueChange = {
            if (it.length <= 6 && it.all { char -> char.isDigit() }) {
                onOtpTextChange(it)
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.NumberPassword
        ),
        decorationBox = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(6) { index ->
                    val char = otpText.getOrNull(index)?.toString() ?: ""
                    val isFilled = char.isNotEmpty()

                    val (borderColor, backgroundColor) = when {
                        isError -> HilingualTheme.colors.alertRed to HilingualTheme.colors.white
                        isFilled -> HilingualTheme.colors.hilingualBlack to HilingualTheme.colors.white
                        else -> Color.Transparent to HilingualTheme.colors.gray100
                    }

                    Box(
                        modifier = Modifier
                            .size(width = 48.dp, height = 60.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(backgroundColor)
                            .border(
                                width = 1.dp,
                                color = borderColor,
                                shape = RoundedCornerShape(8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = char,
                            style = HilingualTheme.typography.headSB20,
                            color = if (isError) HilingualTheme.colors.alertRed else HilingualTheme.colors.black,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun OtpTextFieldPreview() {
    HilingualTheme {
        Column(
            modifier = Modifier.background(white).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var otpText1 by remember { mutableStateOf("") }
            Text("Empty")
            OtpTextField(
                otpText = otpText1,
                onOtpTextChange = { otpText1 = it }
            )

            var otpText2 by remember { mutableStateOf("123") }
            Text("Filled")
            OtpTextField(
                otpText = otpText2,
                onOtpTextChange = { otpText2 = it }
            )

            var otpText3 by remember { mutableStateOf("12345") }
            Text("Error")
            OtpTextField(
                otpText = otpText3,
                onOtpTextChange = { otpText3 = it },
                isError = true
            )

            var otpText4 by remember { mutableStateOf("123456") }
            Text("Full Error")
            OtpTextField(
                otpText = otpText4,
                onOtpTextChange = { otpText4 = it },
                isError = true
            )
        }

    }
}
