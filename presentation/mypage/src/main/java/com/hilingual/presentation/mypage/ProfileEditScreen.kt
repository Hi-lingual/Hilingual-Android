package com.hilingual.presentation.mypage

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
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
import com.hilingual.presentation.mypage.component.ProfileItem
import com.hilingual.presentation.mypage.component.WithdrawDialog

@Composable
internal fun ProfileEditRoute(
    paddingValues: PaddingValues,
    navigateToSplash: () -> Unit,
    viewModel: MyPageViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val dialogTrigger = LocalDialogTrigger.current

    viewModel.sideEffect.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is MyPageSideEffect.ShowRetryDialog -> {
                dialogTrigger.show(sideEffect.onRetry)
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadInitialData()
    }

    when (val state = uiState) {
        is UiState.Success -> {
            ProfileEditScreen(
                paddingValues = paddingValues,
                profileImageUrl = state.data.profileImageUrl,
                profileNickname = state.data.profileNickname,
                onWithdrawClick = navigateToSplash
            )
        }

        else -> {}
    }
}

@Composable
private fun ProfileEditScreen(
    paddingValues: PaddingValues,
    profileImageUrl: String,
    profileNickname: String,
    onWithdrawClick: () -> Unit
) {
    var imageUri by remember { mutableStateOf<String?>(profileImageUrl) }
    var isImageSheetVisible by remember { mutableStateOf(false) }
    var isWithdrawDialogVisible by remember { mutableStateOf(false) }

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            imageUri = uri.toString()
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
            ProfileItem(label = "닉네임", value = profileNickname)
            ProfileItem(label = "연결된 소셜 계정", value = "구글 로그인")
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
            profileImageUrl = "",
            profileNickname = "하링이",
            onWithdrawClick = {}
        )
    }
}
