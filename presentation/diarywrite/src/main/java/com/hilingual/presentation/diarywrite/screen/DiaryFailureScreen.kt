/*
 * Copyright 2026 The Hilingual Project
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
package com.hilingual.presentation.diarywrite.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.component.button.HilingualButton
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.core.ui.component.topappbar.BackTopAppBar
import com.hilingual.core.designsystem.R as DesignSystemR

@Composable
internal fun DiaryFailureScreen(
    paddingValues: PaddingValues,
    onBackClick: () -> Unit,
    onRequestAgainButtonClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(HilingualTheme.colors.white),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            BackTopAppBar(
                onBackClicked = onBackClick,
                title = null,
            )

            Spacer(modifier = Modifier.weight(120f))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    modifier = Modifier
                        .width(200.dp)
                        .height(175.dp),
                    painter = painterResource(DesignSystemR.drawable.img_error_diary),
                    contentDescription = null,
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "피드백을 받는 중에\n일시적인 오류가 발생했어요!",
                    color = HilingualTheme.colors.gray850,
                    style = HilingualTheme.typography.headSB20,
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "잠시 후 다시 시도해주세요.",
                    color = HilingualTheme.colors.gray400,
                    style = HilingualTheme.typography.headR18,
                    textAlign = TextAlign.Center,
                )
            }

            Spacer(modifier = Modifier.weight(193f))

            HilingualButton(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 14.dp),
                text = "피드백 다시 요청하기",
                onClick = onRequestAgainButtonClick,
            )
        }
    }
}

@Preview
@Composable
private fun DiaryFailureScreenPreview() {
    HilingualTheme {
        DiaryFailureScreen(
            paddingValues = PaddingValues(0.dp),
            onBackClick = {},
            onRequestAgainButtonClick = {},
        )
    }
}
