package com.hilingual.presentation.notification.setting.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
internal fun DayChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(37.dp)
            .clip(CircleShape)
            .background(
                if (isSelected) HilingualTheme.colors.hilingualOrange else HilingualTheme.colors.gray200,
            )
            .noRippleClickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            style = HilingualTheme.typography.bodyM15,
            color = if (isSelected) HilingualTheme.colors.white else HilingualTheme.colors.gray500,
        )
    }
}

@Preview
@Composable
private fun DayChipPreview() {
    HilingualTheme {
        Column {
            DayChip(label = "월", isSelected = true, onClick = {})
            DayChip(label = "화", isSelected = false, onClick = {})
        }
    }
}
