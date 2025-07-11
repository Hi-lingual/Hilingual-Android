package com.hilingual.presentation.home.component.footer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.home.R

@Composable
internal fun WriteDiaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .background(HilingualTheme.colors.hilingualBlack)
            .padding(vertical = 18.dp)
            .noRippleClickable(onClick = onClick),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_plus_16),
            contentDescription = null,
            tint = Color.Unspecified
        )

        Spacer(Modifier.width(8.dp))

        Text(
            text = "일기 작성하기",
            style = HilingualTheme.typography.bodySB16,
            color = HilingualTheme.colors.white
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun WriteDiaryButtonPreview() {
    HilingualTheme {
        WriteDiaryButton(
            onClick = {}
        )
    }
}
