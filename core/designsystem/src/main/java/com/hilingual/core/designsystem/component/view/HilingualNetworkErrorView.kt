package com.hilingual.core.designsystem.component.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.designsystem.R
import com.hilingual.core.designsystem.component.topappbar.HilingualBasicTopAppBar
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
fun HilingualNetworkErrorView(
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
    isBackVisible: Boolean = false,
    onBackClick: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(HilingualTheme.colors.white)
            .noRippleClickable { },
    ) {
        if (isBackVisible) {
            HilingualBasicTopAppBar(
                navigationIcon = {
                    Icon(
                        modifier = Modifier
                            .size(24.dp)
                            .noRippleClickable(onClick = onBackClick),
                        imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_left_24_back),
                        contentDescription = null,
                        tint = HilingualTheme.colors.black,
                    )
                },
            )
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    modifier = Modifier
                        .width(200.dp)
                        .height(175.dp),
                    painter = painterResource(R.drawable.img_network_error),
                    contentDescription = null,
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "인터넷 연결이 불안정해요.",
                    color = HilingualTheme.colors.gray850,
                    style = HilingualTheme.typography.headSB20,
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "연결 상태를 확인한 뒤 다시 시도해주세요.",
                    color = HilingualTheme.colors.gray400,
                    style = HilingualTheme.typography.headR18,
                    textAlign = TextAlign.Center,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            RetryButton(
                onClick = onRetryClick,
            )
        }
    }
}

@Composable
private fun RetryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Text(
        text = "다시 시도",
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

@Preview
@Composable
private fun HilingualNetworkErrorViewPreview() {
    HilingualTheme {
        HilingualNetworkErrorView(
            isBackVisible = true,
            onBackClick = {},
            onRetryClick = {},
        )
    }
}
