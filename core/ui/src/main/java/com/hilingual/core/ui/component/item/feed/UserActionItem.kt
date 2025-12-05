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
package com.hilingual.core.ui.component.item.feed

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.core.ui.component.button.UserActionButton
import com.hilingual.core.ui.component.image.ProfileImage

@Composable
fun UserActionItem(
    userId: Long,
    profileUrl: String,
    nickname: String,
    isFilled: Boolean,
    buttonText: String,
    onProfileClick: (Long) -> Unit,
    onButtonClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .background(HilingualTheme.colors.white)
            .padding(vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.noRippleClickable(onClick = { onProfileClick(userId) })
        ) {
            ProfileImage(
                imageUrl = profileUrl,
                modifier = Modifier
                    .size(42.dp)
                    .border(
                        width = 1.dp,
                        color = HilingualTheme.colors.gray200,
                        shape = CircleShape
                    )
            )

            Text(
                text = nickname,
                style = HilingualTheme.typography.headSB16,
                color = HilingualTheme.colors.black
            )
        }

        UserActionButton(
            isFilled = isFilled,
            buttonText = buttonText,
            onClick = { onButtonClick(userId) }
        )
    }
}

@Preview()
@Composable
private fun UserActionItemPreview() {
    HilingualTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            UserActionItem(
                userId = 1L,
                profileUrl = "",
                nickname = "나현",
                isFilled = false,
                buttonText = "팔로우",
                onProfileClick = {},
                onButtonClick = {}
            )
            UserActionItem(
                userId = 2L,
                profileUrl = "",
                nickname = "작나",
                isFilled = false,
                buttonText = "맞팔로우",
                onProfileClick = {},
                onButtonClick = {}
            )
            UserActionItem(
                userId = 3L,
                profileUrl = "",
                nickname = "큰나",
                isFilled = true,
                buttonText = "팔로잉",
                onProfileClick = {},
                onButtonClick = {}
            )
        }
    }
}
