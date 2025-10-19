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
package com.hilingual.presentation.mypage.profileedit

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hilingual.core.common.extension.collectSideEffect
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.common.extension.statusBarColor
import com.hilingual.core.common.trigger.LocalDialogTrigger
import com.hilingual.core.common.util.UiState
import com.hilingual.core.designsystem.component.bottomsheet.HilingualProfileImageBottomSheet
import com.hilingual.core.designsystem.component.picker.ProfileImagePicker
import com.hilingual.core.designsystem.component.topappbar.TitleCenterAlignedTopAppBar
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.mypage.MyPageSideEffect
import com.hilingual.presentation.mypage.MyPageUiState
import com.hilingual.presentation.mypage.MyPageViewModel
import com.hilingual.presentation.mypage.component.ProfileItem
import com.hilingual.presentation.mypage.component.WithdrawDialog
import com.jakewharton.processphoenix.ProcessPhoenix

@Composable
internal fun ProfileEditRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    viewModel: MyPageViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val dialogTrigger = LocalDialogTrigger.current

    viewModel.sideEffect.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is MyPageSideEffect.ShowErrorDialog -> {
                dialogTrigger.show(navigateUp)
            }

            MyPageSideEffect.RestartApp -> {
                ProcessPhoenix.triggerRebirth(context)
            }
        }
    }

    when (val state = uiState) {
        is UiState.Success -> {
            ProfileEditScreen(
                paddingValues = paddingValues,
                profileLoginInfo = state.data,
                onProfileImageUriChanged = viewModel::patchProfileImage,
                onWithdrawClick = viewModel::withdraw
            )
        }

        else -> {}
    }
}

@Composable
private fun ProfileEditScreen(
    paddingValues: PaddingValues,
    profileLoginInfo: MyPageUiState,
    onProfileImageUriChanged: (Uri?) -> Unit,
    onWithdrawClick: () -> Unit
) {
    var imageUri by remember { mutableStateOf<String?>(profileLoginInfo.profileImageUrl.takeIf { it.isNotBlank() }) }
    var isImageSheetVisible by remember { mutableStateOf(false) }
    var isWithdrawDialogVisible by remember { mutableStateOf(false) }

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                imageUri = uri.toString()
                onProfileImageUriChanged(uri)
            }
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarColor(HilingualTheme.colors.white)
            .background(HilingualTheme.colors.white)
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TitleCenterAlignedTopAppBar(
            title = "프로필 작성"
        )

        Spacer(modifier = Modifier.height(46.dp))

        ProfileImagePicker(
            onClick = { isImageSheetVisible = true },
            imageUrl = imageUri
        )

        Spacer(modifier = Modifier.height(48.dp))

        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ProfileItem(label = "닉네임", value = profileLoginInfo.profileNickname)
            ProfileItem(label = "연결된 소셜 계정", value = profileLoginInfo.profileProvider)
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "회원탈퇴",
            color = HilingualTheme.colors.gray400,
            style = HilingualTheme.typography.bodyM14,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier
                .padding(vertical = 4.dp)
                .noRippleClickable(onClick = { isWithdrawDialogVisible = true })
        )

        Spacer(modifier = Modifier.height(16.dp))
    }

    HilingualProfileImageBottomSheet(
        isVisible = isImageSheetVisible,
        onDismiss = { isImageSheetVisible = false },
        onDefaultImageClick = {
            isImageSheetVisible = false
            if (imageUri != null) {
                imageUri = null
                onProfileImageUriChanged(null)
            }
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

    WithdrawDialog(
        isVisible = isWithdrawDialogVisible,
        onDismiss = { isWithdrawDialogVisible = false },
        onDeleteClick = onWithdrawClick
    )
}

@Preview
@Composable
private fun ProfileEditScreenPreview() {
    HilingualTheme {
        ProfileEditScreen(
            paddingValues = PaddingValues(),
            profileLoginInfo = MyPageUiState(
                profileImageUrl = "",
                profileNickname = "하링이",
                profileProvider = "구글 로그인"
            ),
            onProfileImageUriChanged = {},
            onWithdrawClick = {}
        )
    }
}
