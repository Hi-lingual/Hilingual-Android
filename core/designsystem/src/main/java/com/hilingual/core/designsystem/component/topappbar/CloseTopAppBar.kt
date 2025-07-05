package com.hilingual.core.designsystem.component.topappbar

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.designsystem.R
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
fun CloseTopAppBar(
    title: String?,
    onCloseClicked: () -> Unit
) {
    HilingualBasicTopAppBar(
        title = title,
        navigationIcon = {
            Box(
                modifier = Modifier.noRippleClickable(onCloseClicked)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_close_24),
                    contentDescription = null,
                    tint = HilingualTheme.colors.black
                )
            }
        }
    )
}

@Preview
@Composable
private fun CloseTopAppBarPreview() {
    HilingualTheme {
        CloseTopAppBar(
            title = "일기 작성하기",
            onCloseClicked = {}
        )
    }
}