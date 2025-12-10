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
package com.hilingual.presentation.mypage.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
internal fun ProfileItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = HilingualTheme.colors.gray200,
                shape = RoundedCornerShape(8.dp)
            )
            .background(HilingualTheme.colors.white, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 20.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            color = HilingualTheme.colors.black,
            style = HilingualTheme.typography.bodyM16
        )

        Text(
            text = value,
            color = HilingualTheme.colors.gray500,
            style = HilingualTheme.typography.bodyR16
        )
    }
}

@Preview
@Composable
private fun ProfileItemPreview() {
    HilingualTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ProfileItem(label = "닉네임", value = "내 닉네임")
            ProfileItem(label = "연결된 소셜 계정", value = "구글 로그인")
        }
    }
}
