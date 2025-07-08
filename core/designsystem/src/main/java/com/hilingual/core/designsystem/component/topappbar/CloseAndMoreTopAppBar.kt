package com.hilingual.core.designsystem.component.topappbar

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.designsystem.R
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
fun CloseAndMoreTopAppBar(
    modifier: Modifier = Modifier,
    title: String?,
    onCloseClicked: () -> Unit,
    onMoreClicked: () -> Unit
) {
    HilingualBasicTopAppBar(
        modifier = modifier,
        title = title,
        navigationIcon = {
            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .noRippleClickable(onClick = onCloseClicked),
                imageVector = ImageVector.vectorResource(R.drawable.ic_close_24),
                contentDescription = null,
                tint = HilingualTheme.colors.black
            )
        },
        actions = {
            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .noRippleClickable(onClick = onMoreClicked),
                imageVector = ImageVector.vectorResource(R.drawable.ic_more_24),
                contentDescription = null,
                tint = HilingualTheme.colors.gray400
            )
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