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
package com.hilingual.presentation.onboarding.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.common.constant.UrlConstant
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.designsystem.component.bottomsheet.HilingualBasicBottomSheet
import com.hilingual.core.designsystem.component.button.HilingualButton
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.onboarding.R
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentMap

private enum class TermType {
    REQUIRED,
    MARKETING
}

private data class Term(
    val text: String,
    val type: TermType,
    val link: String? = null
) {
    val isRequired: Boolean
        get() = type == TermType.REQUIRED
}

private val terms = persistentListOf(
    Term(
        text = "서비스 이용약관 동의 (필수)",
        type = TermType.REQUIRED,
        link = UrlConstant.SERVICE_TERMS
    ),
    Term(
        text = "개인정보 수집 및 이용 동의 (필수)",
        type = TermType.REQUIRED,
        link = UrlConstant.ONBOARDING_PRIVACY_POLICY
    ),
    Term(
        text = "앱 내 광고성 정보 수신 동의 (선택)",
        type = TermType.MARKETING
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TermsBottomSheet(
    isVisible: Boolean,
    isLoading: Boolean,
    onDismiss: () -> Unit,
    onStartClick: (isMarketingAgreed: Boolean) -> Unit,
    onTermLinkClick: (url: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var agreements by remember { mutableStateOf(terms.associateWith { false }.toPersistentMap()) }

    val allAgreed = agreements.values.all { it }

    val isStartButtonEnabled = remember(agreements) {
        agreements.filterKeys { it.isRequired }.values.all { it }
    }

    val isMarketingAgreed = remember(agreements) {
        agreements.filterKeys { it.type == TermType.MARKETING }.values.firstOrNull() ?: false
    }

    val onAllAgreedClick: () -> Unit = {
        val newState = !allAgreed
        agreements = terms.associateWith { newState }.toPersistentMap()
    }

    HilingualBasicBottomSheet(
        isVisible = isVisible,
        onDismiss = onDismiss,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 32.dp, bottom = 12.dp)
        ) {
            Text(
                text = "하이링구얼이 처음이시군요!",
                style = HilingualTheme.typography.headSB18,
                color = HilingualTheme.colors.black
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = "아래 약관에 동의 후 서비스 이용이 가능해요.",
                style = HilingualTheme.typography.bodyR14,
                color = HilingualTheme.colors.gray400
            )

            Spacer(Modifier.height(32.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .fillMaxWidth()
                        .background(HilingualTheme.colors.gray100)
                        .clickable(onClick = onAllAgreedClick)
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "전체 동의",
                        style = HilingualTheme.typography.headSB18,
                        color = HilingualTheme.colors.black
                    )

                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_check_circle_28_and),
                        tint = if (allAgreed) HilingualTheme.colors.black else Color.Unspecified,
                        contentDescription = null
                    )
                }

                agreements.forEach { (term, isAgreed) ->
                    key(term) {
                        TermRow(
                            text = term.text,
                            isSelected = isAgreed,
                            onSelectedChange = { isSelected ->
                                agreements = agreements.put(term, isSelected)
                            },
                            onTextClick = term.link?.let { { onTermLinkClick(it) } }
                        )
                    }
                }
            }

            Spacer(Modifier.height(64.dp))

            HilingualButton(
                text = "시작하기",
                onClick = { onStartClick(isMarketingAgreed) },
                enableProvider = { isStartButtonEnabled && !isLoading }
            )
        }
    }
}

@Composable
private fun TermRow(
    text: String,
    isSelected: Boolean,
    onSelectedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    onTextClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = HilingualTheme.typography.bodyM14,
            color = HilingualTheme.colors.gray400,
            textDecoration = if (onTextClick != null) TextDecoration.Underline else TextDecoration.None,
            modifier = if (onTextClick != null) Modifier.noRippleClickable(onClick = onTextClick) else Modifier
        )

        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_check_circle_28_and),
            tint = if (isSelected) HilingualTheme.colors.black else Color.Unspecified,
            modifier = Modifier.noRippleClickable(onClick = { onSelectedChange(!isSelected) }),
            contentDescription = null
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun TermsBottomSheetPreview() {
    var isVisible by remember { mutableStateOf(true) }
    HilingualTheme {
        TermsBottomSheet(
            isVisible = isVisible,
            onDismiss = { isVisible = false },
            onStartClick = {},
            isLoading = false,
            onTermLinkClick = {}
        )
    }
}
