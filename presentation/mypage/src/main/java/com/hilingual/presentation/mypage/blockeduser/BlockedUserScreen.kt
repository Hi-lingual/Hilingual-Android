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
package com.hilingual.presentation.mypage.blockeduser

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hilingual.core.common.extension.collectSideEffect
import com.hilingual.core.common.extension.statusBarColor
import com.hilingual.core.common.trigger.LocalDialogTrigger
import com.hilingual.core.common.util.UiState
import com.hilingual.core.designsystem.R
import com.hilingual.core.designsystem.component.content.UserActionItem
import com.hilingual.core.designsystem.component.topappbar.BackTopAppBar
import com.hilingual.core.designsystem.theme.HilingualTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

@Composable
internal fun BlockedUserRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToProfile: (Long) -> Unit,
    viewModel: BlockedUserViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val dialogTrigger = LocalDialogTrigger.current

    viewModel.sideEffect.collectSideEffect { sideEffect ->
        if (sideEffect is BlockedUserSideEffect.ShowErrorDialog) dialogTrigger.show(navigateUp)
    }

    LaunchedEffect(Unit) {
        viewModel.getBlockList()
    }

    when (val state = uiState.blockedUserList) {
        is UiState.Success -> {
            BlockedUserScreen(
                paddingValues = paddingValues,
                onBackClick = navigateUp,
                blockedUserList = state.data,
                onUserProfileClick = navigateToProfile,
                onButtonClick = viewModel::onUnblockStatusChanged
            )
        }

        else -> {}
    }
}

@Composable
private fun BlockedUserScreen(
    paddingValues: PaddingValues,
    onBackClick: () -> Unit,
    blockedUserList: ImmutableList<BlockedUserUiModel>,
    onUserProfileClick: (Long) -> Unit,
    onButtonClick: (Long) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarColor(HilingualTheme.colors.white)
            .background(HilingualTheme.colors.white)
            .padding(paddingValues)
    ) {
        BackTopAppBar(
            title = "차단한 유저",
            onBackClicked = onBackClick
        )

        if (blockedUserList.isEmpty()) {
            Spacer(modifier = Modifier.height(140.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)

            ) {
                Image(
                    modifier = Modifier
                        .size(width = 200.dp, height = 140.dp),
                    painter = painterResource(id = R.drawable.img_search),
                    contentDescription = null
                )
                Text(
                    text = "차단된 계정이 없습니다.",
                    style = HilingualTheme.typography.headM18,
                    color = HilingualTheme.colors.gray500,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                items(
                    items = blockedUserList,
                    key = { it.userId }
                ) { blockedUser ->
                    with(blockedUser) {
                        UserActionItem(
                            userId = userId,
                            profileUrl = profileImageUrl,
                            nickname = nickname,
                            isFilled = !isBlocked,
                            buttonText = if (isBlocked) "차단 해제" else "차단",
                            onProfileClick = onUserProfileClick,
                            onButtonClick = onButtonClick
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun BlockedUserScreenEmptyPreview() {
    HilingualTheme {
        BlockedUserScreen(
            paddingValues = PaddingValues(),
            onBackClick = {},
            blockedUserList = persistentListOf(),
            onUserProfileClick = {},
            onButtonClick = {}
        )
    }
}

@Preview
@Composable
private fun BlockedUserScreenWithDataPreview() {
    HilingualTheme {
        var sampleBlockedUsers by remember {
            mutableStateOf(
                persistentListOf(
                    BlockedUserUiModel(
                        userId = 1L,
                        profileImageUrl = "",
                        nickname = "사과"
                    ),
                    BlockedUserUiModel(
                        userId = 2L,
                        profileImageUrl = "",
                        nickname = "바나나"
                    ),
                    BlockedUserUiModel(
                        userId = 3L,
                        profileImageUrl = "",
                        nickname = "오렌지"
                    ),
                    BlockedUserUiModel(
                        userId = 4L,
                        profileImageUrl = "https://picsum.photos/42/42?random=1",
                        nickname = "딸기"
                    ),
                    BlockedUserUiModel(
                        userId = 5L,
                        profileImageUrl = "",
                        nickname = "포도"
                    )
                )
            )
        }

        BlockedUserScreen(
            paddingValues = PaddingValues(),
            onBackClick = {},
            blockedUserList = sampleBlockedUsers,
            onUserProfileClick = {},
            onButtonClick = { userId ->
                sampleBlockedUsers = sampleBlockedUsers.map { user ->
                    if (user.userId == userId) user.copy(isBlocked = !user.isBlocked) else user
                }.toPersistentList()
            }
        )
    }
}
