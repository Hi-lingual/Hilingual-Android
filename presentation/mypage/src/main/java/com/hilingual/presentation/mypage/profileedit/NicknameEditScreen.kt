package com.hilingual.presentation.mypage.profileedit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hilingual.core.common.extension.addFocusCleaner
import com.hilingual.core.common.extension.statusBarColor
import com.hilingual.core.common.extension.subScreenPadding
import com.hilingual.core.designsystem.component.button.HilingualButton
import com.hilingual.core.designsystem.component.textfield.HilingualShortTextField
import com.hilingual.core.designsystem.component.textfield.TextFieldState
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.core.ui.component.topappbar.BackTopAppBar
import com.hilingual.presentation.mypage.MyPageViewModel

@Composable
internal fun NicknameEditRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    viewModel: MyPageViewModel,
) {
    val nicknameEditState by viewModel.nicknameEditState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.initNicknameEdit()
    }

    var textFieldState by remember { mutableStateOf(TextFieldState.NORMAL) }

    LaunchedEffect(nicknameEditState.isNicknameValid, nicknameEditState.validationMessage, nicknameEditState.nickname) {
        if (nicknameEditState.nickname.isNotEmpty()) {
            textFieldState = when {
                nicknameEditState.isNicknameValid -> TextFieldState.SUCCESS
                nicknameEditState.validationMessage.isNotEmpty() -> TextFieldState.ERROR
                else -> TextFieldState.NORMAL
            }
        }
    }

    NicknameEditScreen(
        paddingValues = paddingValues,
        onBackClick = navigateUp,
        nickname = { nicknameEditState.nickname },
        onNicknameChanged = { newNickname ->
            textFieldState = TextFieldState.NORMAL
            viewModel.onNicknameInputChanged(newNickname)
        },
        textFieldState = { textFieldState },
        validationMessage = { nicknameEditState.validationMessage },
        isNicknameValid = { nicknameEditState.isNicknameValid },
        onDoneAction = viewModel::onSubmitNickname,
        onConfirmClick = {},
    )
}

@Composable
private fun NicknameEditScreen(
    paddingValues: PaddingValues,
    onBackClick: () -> Unit,
    nickname: () -> String,
    onNicknameChanged: (String) -> Unit,
    textFieldState: () -> TextFieldState,
    validationMessage: () -> String,
    isNicknameValid: () -> Boolean,
    onDoneAction: (String) -> Unit,
    onConfirmClick: () -> Unit,
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarColor(HilingualTheme.colors.white)
            .background(HilingualTheme.colors.white)
            .subScreenPadding(paddingValues)
            .addFocusCleaner(focusManager),
    ) {
        BackTopAppBar(
            title = "닉네임 변경",
            onBackClicked = onBackClick,
        )

        Spacer(modifier = Modifier.height(32.dp))

        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
        ) {
            Text(
                text = "닉네임",
                style = HilingualTheme.typography.bodyM16,
                color = HilingualTheme.colors.black,
                modifier = Modifier.align(Alignment.Start),
            )

            Spacer(modifier = Modifier.height(4.dp))

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
                },
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        HilingualButton(
            text = "변경하기",
            onClick = onConfirmClick,
            enableProvider = isNicknameValid,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
        )
    }
}

@Preview
@Composable
private fun NicknameEditScreenPreview() {
    HilingualTheme {
        NicknameEditScreen(
            paddingValues = PaddingValues(),
            onBackClick = {},
            nickname = { "하링이" },
            onNicknameChanged = {},
            textFieldState = { TextFieldState.NORMAL },
            validationMessage = { "" },
            isNicknameValid = { false },
            onDoneAction = {},
            onConfirmClick = {},
        )
    }
}
