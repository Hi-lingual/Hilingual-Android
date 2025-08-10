package com.hilingual.presentation.otp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.hilingual.core.common.extension.addFocusCleaner
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.otp.component.OtpTextField

@Composable
fun OtpScreen(
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .addFocusCleaner(focusManager)
            .background(HilingualTheme.colors.white)
            .padding(paddingValues)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        var otpText by remember { mutableStateOf("") }

        OtpTextField(
            otpText = { otpText },
            onOtpTextChange = { otpText = it }
        )
    }
}
