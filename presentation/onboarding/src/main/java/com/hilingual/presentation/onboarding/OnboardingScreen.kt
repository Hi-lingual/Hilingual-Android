package com.hilingual.presentation.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hilingual.core.designsystem.component.button.HilingualButton
import com.hilingual.core.designsystem.component.textfield.HilingualShortTextField
import com.hilingual.core.designsystem.component.topappbar.HilingualBasicTopAppBar
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.core.designsystem.theme.white
import com.hilingual.core.designsystem.R as DesignSystemR

@Composable
internal fun OnboardingRoute(
    paddingValues: PaddingValues,
    navigateToHome: () -> Unit
) {
    val systemUiController = rememberSystemUiController()
    var value by remember { mutableStateOf("") }

    SideEffect {
        systemUiController.setStatusBarColor(color = white, darkIcons = false)
    }

    OnboardingScreen(
        paddingValues = paddingValues,
        value = value,
        onValueChanged = { value = it },
        isValid = { true },
        errorMessage = "",
        onDoneAction = {},
        onButtonClick = navigateToHome
    )
}

@Composable
private fun OnboardingScreen(
    paddingValues: PaddingValues,
    value: String,
    onValueChanged: (String) -> Unit,
    isValid: () -> Boolean,
    errorMessage: String,
    onDoneAction: () -> Unit,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(HilingualTheme.colors.white)
            .padding(paddingValues)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HilingualBasicTopAppBar(title = "프로필 작성")

        Spacer(Modifier.weight(13f))

        Box {
            Image(
                painter = painterResource(DesignSystemR.drawable.img_default_image),
                modifier = Modifier
                    .size(120.dp)
                    .clip(shape = CircleShape)
                    .border(
                        shape = CircleShape,
                        color = HilingualTheme.colors.gray200,
                        width = 1.dp
                    ),
                contentDescription = null
            )
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_onboarding_camera_32),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.align(Alignment.BottomEnd)
            )
        }

        Spacer(Modifier.weight(8f))

        Text(
            text = "닉네임",
            style = HilingualTheme.typography.bodySB16,
            color = HilingualTheme.colors.black,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(Modifier.height(4.dp))

        HilingualShortTextField(
            value = value,
            onValueChanged = onValueChanged,
            placeholder = "한글, 영문, 숫자 조합만 가능",
            maxLength = 10,
            isValid = isValid,
            errorMessage = errorMessage,
            successMessage = "사용 가능한 닉네임이에요",
            onDoneAction = onDoneAction
        )

        Spacer(Modifier.weight(79f))

        HilingualButton(
            text = "시작하기",
            onClick = onButtonClick,
            enabled = isValid(),
            modifier = Modifier.padding(vertical = 12.dp)
        )
    }
}

@Preview
@Composable
private fun OnboardingScreenPreview() {
    HilingualTheme {
        OnboardingScreen(
            paddingValues = PaddingValues(0.dp),
            value = "",
            onValueChanged = {},
            isValid = { true },
            errorMessage = "",
            onDoneAction = {},
            onButtonClick = {}
        )
    }
}
