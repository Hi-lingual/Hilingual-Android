package com.hilingual.presentation.voca.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.core.designsystem.R as DesignSystemR

@Composable
internal fun MemorizedBadge(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(DesignSystemR.drawable.ic_check_circle_16),
            contentDescription = null,
            tint = Color.Unspecified,
        )
        Text(
            text = "아는 단어",
            style = HilingualTheme.typography.captionR12,
            color = HilingualTheme.colors.gray500,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MemorizedBadgePreview() {
    HilingualTheme {
        MemorizedBadge()
    }
}
