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
package com.hilingual.core.designsystem.component.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.common.analytics.Page
import com.hilingual.core.common.analytics.TriggerType
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.common.model.LoadErrorHandleAction
import com.hilingual.core.common.provider.LocalTracker
import com.hilingual.core.designsystem.R
import com.hilingual.core.designsystem.component.topappbar.HilingualBasicTopAppBar
import com.hilingual.core.designsystem.theme.HilingualTheme

/**
 * @param page 이벤트 수집 대상 화면. null이면 이벤트를 수집하지 않는다.
 */
@Composable
fun HilingualLoadErrorView(
    action: LoadErrorViewAction,
    modifier: Modifier = Modifier,
    page: Page? = null,
) {
    val content = action.content
    val tracker = LocalTracker.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(HilingualTheme.colors.white),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            action.onBackClick?.let { onBackClick ->
                HilingualBasicTopAppBar(
                    navigationIcon = {
                        Icon(
                            modifier = Modifier
                                .size(24.dp)
                                .noRippleClickable {
                                    page?.let {
                                        tracker.logPageAction(
                                            trigger = TriggerType.CLICK,
                                            page = it,
                                            action = "server_error_go_back",
                                        )
                                    }
                                    onBackClick()
                                },
                            imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_left_24_back),
                            contentDescription = null,
                            tint = HilingualTheme.colors.black,
                        )
                    },
                )
            }

            Spacer(Modifier.weight(2f))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    modifier = Modifier
                        .width(200.dp)
                        .height(175.dp),
                    painter = painterResource(R.drawable.img_error),
                    contentDescription = null,
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = content.title,
                    color = HilingualTheme.colors.gray850,
                    style = HilingualTheme.typography.headSB20,
                    textAlign = TextAlign.Center,
                )

                content.description?.let { description ->
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = description,
                        color = HilingualTheme.colors.gray400,
                        style = HilingualTheme.typography.headR18,
                        textAlign = TextAlign.Center,
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                ActionButton(
                    text = content.buttonText,
                    onClick = {
                        page?.let {
                            tracker.logPageAction(
                                trigger = TriggerType.CLICK,
                                page = it,
                                action = action.handleAction.eventName,
                            )
                        }
                        action.onActionButtonClick()
                    },
                )
            }

            Spacer(Modifier.weight(3f))
        }
    }
}

@Stable
sealed class LoadErrorViewAction private constructor(
    internal val handleAction: LoadErrorHandleAction,
    internal val onActionButtonClick: () -> Unit,
    internal open val onBackClick: (() -> Unit)?,
) {
    data class Retry(
        val onRetryClick: () -> Unit,
        override val onBackClick: (() -> Unit)? = null,
    ) : LoadErrorViewAction(
        handleAction = LoadErrorHandleAction.Retry,
        onActionButtonClick = onRetryClick,
        onBackClick = onBackClick,
    )

    data class Back(
        override val onBackClick: () -> Unit,
    ) : LoadErrorViewAction(
        handleAction = LoadErrorHandleAction.Back,
        onActionButtonClick = onBackClick,
        onBackClick = onBackClick,
    )

    data class NotFound(
        override val onBackClick: () -> Unit,
    ) : LoadErrorViewAction(
        handleAction = LoadErrorHandleAction.NotFound,
        onActionButtonClick = onBackClick,
        onBackClick = onBackClick,
    )
}

@Composable
private fun ActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        style = HilingualTheme.typography.bodyM16,
        color = HilingualTheme.colors.white,
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(
                shape = RoundedCornerShape(8.dp),
                color = HilingualTheme.colors.hilingualBlack,
            )
            .width(IntrinsicSize.Max)
            .noRippleClickable(onClick = onClick)
            .padding(12.dp),
        textAlign = TextAlign.Center,
    )
}

private data class LoadErrorContent(
    val title: String,
    val description: String?,
    val buttonText: String,
)

private val LoadErrorHandleAction.eventName: String
    get() = when (this) {
        LoadErrorHandleAction.Retry -> "server_error_retry"
        LoadErrorHandleAction.Back -> "server_error_go_back"
        LoadErrorHandleAction.NotFound -> "empty_data_confirm"
    }

private val LoadErrorViewAction.content: LoadErrorContent
    get() {
        return when (handleAction) {
            LoadErrorHandleAction.Retry -> LoadErrorContent(
                title = "일시적인 오류가 발생해\n내용을 불러오지 못했어요.",
                description = null,
                buttonText = "다시 시도",
            )

            LoadErrorHandleAction.Back -> LoadErrorContent(
                title = "정보를 불러오지 못했어요.",
                description = "이전 화면으로 돌아가 다시 확인 해주세요.",
                buttonText = "이전 페이지로 돌아가기",
            )

            LoadErrorHandleAction.NotFound -> LoadErrorContent(
                title = "요청한 내용을 찾을 수 없어요",
                description = "삭제되었거나 더 이상\n제공되지 않는 내용이에요",
                buttonText = "이전 페이지로 돌아가기",
            )
        }
    }

@Preview(name = "Retry")
@Composable
private fun HilingualLoadErrorRetryViewPreview() {
    HilingualTheme {
        HilingualLoadErrorView(
            action = LoadErrorViewAction.Retry(
                onRetryClick = {},
            ),
        )
    }
}

@Preview(name = "Back")
@Composable
private fun HilingualLoadErrorBackViewPreview() {
    HilingualTheme {
        HilingualLoadErrorView(
            action = LoadErrorViewAction.Back(
                onBackClick = {},
            ),
        )
    }
}

@Preview(name = "Not Found")
@Composable
private fun HilingualLoadErrorNotFoundViewPreview() {
    HilingualTheme {
        HilingualLoadErrorView(
            action = LoadErrorViewAction.NotFound(
                onBackClick = {},
            ),
        )
    }
}
