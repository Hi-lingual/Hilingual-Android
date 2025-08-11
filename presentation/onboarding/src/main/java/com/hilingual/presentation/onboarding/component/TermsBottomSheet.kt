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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TermsBottomSheet(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onStartClick: (isMarketingAgreed: Boolean) -> Unit,
    onTermLinkClick: (url: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var serviceAgreed by remember { mutableStateOf(false) }
    var privacyAgreed by remember { mutableStateOf(false) }
    var marketingAgreed by remember { mutableStateOf(false) }

    val allAgreed = serviceAgreed && privacyAgreed && marketingAgreed
    val isStartButtonEnabled = serviceAgreed && privacyAgreed

    val onAllAgreedClick: () -> Unit = {
        val newState = !allAgreed
        serviceAgreed = newState
        privacyAgreed = newState
        marketingAgreed = newState
    }

    if (isVisible) {
        HilingualBasicBottomSheet(
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
                    style = HilingualTheme.typography.headB18,
                    color = HilingualTheme.colors.black
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = "아래 약관에 동의 후 서비스 이용이 가능해요.",
                    style = HilingualTheme.typography.bodyM14,
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
                            style = HilingualTheme.typography.headB18,
                            color = HilingualTheme.colors.black
                        )

                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_check_circle_28_and),
                            tint = if (allAgreed) HilingualTheme.colors.black else Color.Unspecified,
                            contentDescription = null
                        )
                    }

                    TermRow(
                        text = "서비스 이용약관 동의 (필수)",
                        isSelected = serviceAgreed,
                        onSelectedChange = { serviceAgreed = it },
                        onTextClick = { onTermLinkClick(UrlConstant.SERVICE_TERMS) }
                    )
                    TermRow(
                        text = "개인정보 수집 및 이용 동의 (필수)",
                        isSelected = privacyAgreed,
                        onSelectedChange = { privacyAgreed = it },
                        onTextClick = { onTermLinkClick(UrlConstant.ONBOARDING_PRIVACY_POLICY) }
                    )
                    TermRow(
                        text = "광고성 정보 수신 동의 (선택)",
                        isSelected = marketingAgreed,
                        onSelectedChange = { marketingAgreed = it }
                    )
                }

                Spacer(Modifier.height(64.dp))

                HilingualButton(
                    text = "시작하기",
                    onClick = { onStartClick(marketingAgreed) },
                    enableProvider = { isStartButtonEnabled }
                )
            }
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
            style = HilingualTheme.typography.bodySB14,
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
            onTermLinkClick = {}
        )
    }
}
