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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hilingual.core.common.extension.addFocusCleaner
import com.hilingual.core.common.provider.LocalSystemBarsColor
import com.hilingual.core.designsystem.component.button.HilingualButton
import com.hilingual.core.designsystem.component.textfield.HilingualShortTextField
import com.hilingual.core.designsystem.component.textfield.TextFieldState
import com.hilingual.core.designsystem.component.topappbar.HilingualBasicTopAppBar
import com.hilingual.core.designsystem.event.LocalDialogController
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.core.designsystem.theme.white
import com.hilingual.core.designsystem.R as DesignSystemR

@Composable
internal fun OnboardingRoute(
    paddingValues: PaddingValues,
    navigateToHome: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val localSystemBarsColor = LocalSystemBarsColor.current
    val dialogController = LocalDialogController.current

    var textFieldState by remember { mutableStateOf(TextFieldState.NORMAL) }

    LaunchedEffect(uiState.isNicknameValid, uiState.validationMessage, uiState.nickname) {
        if (uiState.nickname.isNotEmpty()) {
            textFieldState = when {
                uiState.isNicknameValid -> TextFieldState.SUCCESS
                uiState.validationMessage.isNotEmpty() -> TextFieldState.ERROR
                else -> TextFieldState.NORMAL
            }
        }
    }

    LaunchedEffect(viewModel.sideEffect) {
        viewModel.sideEffect.collect { event ->
            when (event) {
                is OnboardingSideEffect.NavigateToHome -> navigateToHome()
                is OnboardingSideEffect.ShowRetryDialog -> {
                    dialogController.show(event.onRetry)
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        localSystemBarsColor.setSystemBarColor(
            systemBarsColor = white
        )
    }

    OnboardingScreen(
        paddingValues = paddingValues,
        nickname = { uiState.nickname },
        onNicknameChanged = {
            textFieldState = TextFieldState.NORMAL
            viewModel.onNicknameChanged(it)
        },
        textFieldState = { textFieldState },
        validationMessage = { uiState.validationMessage },
        isNicknameValid = { uiState.isNicknameValid },
        onDoneAction = viewModel::onSubmitNickname,
        onButtonClick = viewModel::onRegisterClick
    )
}

@Composable
private fun OnboardingScreen(
    paddingValues: PaddingValues,
    nickname: () -> String,
    onNicknameChanged: (String) -> Unit,
    textFieldState: () -> TextFieldState,
    validationMessage: () -> String,
    isNicknameValid: () -> Boolean,
    onDoneAction: (String) -> Unit,
    onButtonClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(HilingualTheme.colors.white)
            .padding(paddingValues)
            .padding(horizontal = 16.dp)
            .addFocusCleaner(focusManager),
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
            value = nickname,
            onValueChanged = onNicknameChanged,
            placeholder = "한글, 영문, 숫자 조합만 가능",
            maxLength = 10,
            state = textFieldState,
            errorMessage = validationMessage,
            successMessage = "사용 가능한 닉네임이에요",
            onDoneAction = {
                onDoneAction(nickname())
                focusManager.clearFocus()
            }
        )

        Spacer(Modifier.weight(79f))

        HilingualButton(
            text = "시작하기",
            onClick = { onButtonClick(nickname()) },
            enableProvider = isNicknameValid,
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
            nickname = { "" },
            onNicknameChanged = {},
            textFieldState = { TextFieldState.NORMAL },
            validationMessage = { "" },
            isNicknameValid = { true },
            onDoneAction = { _ -> },
            onButtonClick = { _ -> }
        )
    }
}
