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
fun BackTopAppBar(
    modifier: Modifier = Modifier,
    title: String?,
    onBackClicked: () -> Unit
) {
    HilingualBasicTopAppBar(
        modifier = modifier,
        title = title,
        navigationIcon = {
            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .noRippleClickable(onClick = onBackClicked),
                imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_left_24),
                contentDescription = null,
                tint = HilingualTheme.colors.black
            )
        }
    )
}

@Preview
@Composable
private fun BackTopAppBarPreview() {
    HilingualTheme {
        BackTopAppBar(
            title = "일기 작성하기",
            onBackClicked = { }
        )
    }
}