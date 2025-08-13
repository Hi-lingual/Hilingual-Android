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
package com.hilingual.presentation.onboarding

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hilingual.core.common.extension.addFocusCleaner
import com.hilingual.core.common.extension.collectSideEffect
import com.hilingual.core.common.extension.launchCustomTabs
import com.hilingual.core.common.provider.LocalSystemBarsColor
import com.hilingual.core.designsystem.component.bottomsheet.HilingualProfileImageBottomSheet
import com.hilingual.core.designsystem.component.button.HilingualButton
import com.hilingual.core.designsystem.component.picker.ProfileImagePicker
import com.hilingual.core.designsystem.component.textfield.HilingualShortTextField
import com.hilingual.core.designsystem.component.textfield.TextFieldState
import com.hilingual.core.designsystem.component.topappbar.HilingualBasicTopAppBar
import com.hilingual.core.designsystem.event.LocalDialogEventProvider
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.core.designsystem.theme.white
import com.hilingual.presentation.onboarding.component.TermsBottomSheet

@Composable
internal fun OnboardingRoute(
    paddingValues: PaddingValues,
    navigateToHome: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val localSystemBarsColor = LocalSystemBarsColor.current
    val dialogEventProvider = LocalDialogEventProvider.current
    val context = LocalContext.current

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

    viewModel.sideEffect.collectSideEffect {
        when (it) {
            is OnboardingSideEffect.NavigateToHome -> navigateToHome()
            is OnboardingSideEffect.ShowRetryDialog -> {
                dialogEventProvider.show(it.onRetry)
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
        onRegisterClick = viewModel::onRegisterClick,
        onTermLinkClick = { url -> context.launchCustomTabs(url) }
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
    onRegisterClick: (String, Boolean, Uri?) -> Unit,
    onTermLinkClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var isImageSheetVisible by remember { mutableStateOf(false) }
    var isTermsSheetVisible by remember { mutableStateOf(false) }

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            imageUri = uri
        }
    )

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

        ProfileImagePicker(
            onClick = { isImageSheetVisible = true },
            imageUrl = imageUri?.toString()
        )

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
            text = "가입하기",
            onClick = { isTermsSheetVisible = true },
            enableProvider = isNicknameValid,
            modifier = Modifier.padding(vertical = 12.dp)
        )
    }

    TermsBottomSheet(
        isVisible = isTermsSheetVisible,
        onDismiss = { isTermsSheetVisible = false },
        onStartClick = { isMarketingAgreed ->
            onRegisterClick(nickname(), isMarketingAgreed, imageUri)
        },
        onTermLinkClick = onTermLinkClick
    )

    HilingualProfileImageBottomSheet(
        isVisible = isImageSheetVisible,
        onDismiss = { isImageSheetVisible = false },
        onDefaultImageClick = {
            isImageSheetVisible = false
            imageUri = null
        },
        onGalleryImageClick = {
            isImageSheetVisible = false
            singlePhotoPickerLauncher.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly
                )
            )
        }
    )
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
            onRegisterClick = { _, _, _ -> },
            onTermLinkClick = {}
        )
    }
}
