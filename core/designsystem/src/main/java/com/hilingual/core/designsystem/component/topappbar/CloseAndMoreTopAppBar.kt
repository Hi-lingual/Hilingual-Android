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
fun CloseAndMoreTopAppBar(
    title: String?,
    onCloseClicked: () -> Unit,
    onMoreClicked: () -> Unit
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
        },
        actions = {
            Box(
                modifier = Modifier.noRippleClickable(onMoreClicked)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_more_24),
                    contentDescription = null,
                    tint = HilingualTheme.colors.gray400
                )
            }
        }
    )
}

@Preview
@Composable
private fun CloseAndMoreTopAppBarPreview() {
    HilingualTheme {
        CloseAndMoreTopAppBar(
            title = "일기장",
            onCloseClicked = {},
            onMoreClicked = {}
        )
    }
}