package com.hilingual.core.designsystem.component.topappbar

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.designsystem.R
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
fun CloseOnlyTopAppBar(
    modifier: Modifier = Modifier,
    onCloseClicked: () -> Unit
) {
    HilingualBasicTopAppBar(
        modifier = modifier,
        navigationIcon = {
            Icon(
                modifier = Modifier.noRippleClickable(onClick = onCloseClicked),
                imageVector = ImageVector.vectorResource(R.drawable.ic_close_24),
                contentDescription = null,
                tint = HilingualTheme.colors.white
            )
        },
        backgroundColor = Color.Transparent
    )
}

@Preview
@Composable
private fun CloseOnlyTopAppBarPreview() {
    HilingualTheme {
        CloseOnlyTopAppBar(
            onCloseClicked = {}
        )
    }
}
