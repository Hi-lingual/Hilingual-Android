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
package com.hilingual.presentation.notification.detail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichText

@Composable
internal fun NotificationDetailContent(
    title: String,
    date: String,
    content: String,
    modifier: Modifier = Modifier
) {
    val textState = rememberRichTextState()
    val colors = HilingualTheme.colors

    LaunchedEffect(content) {
        textState.config.linkColor = colors.black
        textState.config.linkTextDecoration = TextDecoration.None
        textState.setMarkdown(content)
    }

    Column(
        modifier = modifier
            .background(HilingualTheme.colors.white)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = title,
                style = HilingualTheme.typography.headSB20,
                color = HilingualTheme.colors.black
            )
            Text(
                text = date,
                style = HilingualTheme.typography.bodyR14,
                color = HilingualTheme.colors.gray300
            )
        }
        HorizontalDivider(
            thickness = 1.dp,
            color = HilingualTheme.colors.gray100
        )

        RichText(
            state = textState,
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 16.dp),
            style = HilingualTheme.typography.bodyR14,
            color = HilingualTheme.colors.gray850
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NotificationDetailContentPreview() {
    HilingualTheme {
        NotificationDetailContent(
            title = "v 1.1.0 업데이트 알림",
            date = "2025.08.05",
            content = """
                        # 공지사항 제목

                        안녕하세요, **Compose Rich Editor** 라이브러리 테스트입니다.
                        이 문자열은 서버에서 내려온다고 가정합니다.

                        ## 주요 기능
                        - **볼드**와 *이탤릭*을 모두 지원합니다.
                        - [Google](https://google.com)과 같은 하이퍼링크도 ~~문제 없습니다~~.
                        - 목록 테스트:
                          - 순서 없는 목록 (Unordered List)
                            - 1단계 중첩
                            - 2단계 중첩
                          - 간단한 아이템

                        ## 작업 순서
                        1. 첫 번째 항목입니다.
                        2. `inline code`와 같은 코드 스니펫 작동합니다.

                        감사합니다.
            """.trimIndent()
        )
    }
}
