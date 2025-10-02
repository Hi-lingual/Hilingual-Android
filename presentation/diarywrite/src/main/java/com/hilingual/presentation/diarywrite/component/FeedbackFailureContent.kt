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
package com.hilingual.presentation.diarywrite.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.component.button.HilingualButton
import com.hilingual.core.designsystem.component.topappbar.CloseOnlyTopAppBar
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
internal fun FeedbackFailureContent(
    onCloseButtonClick: () -> Unit,
    onRequestAgainButtonClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        CloseOnlyTopAppBar(
            onCloseClicked = onCloseButtonClick,
            iconTint = HilingualTheme.colors.black
        )

        HilingualButton(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 14.dp),
            text = "다시 요청하기",
            onClick = onRequestAgainButtonClick
        )
    }
}
