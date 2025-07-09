package com.hilingual.presentation.diarywrite.component

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.diarywrite.R

@Composable
internal fun PhotoSelectButton(
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(80.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(HilingualTheme.colors.gray100)
            .noRippleClickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier.size(20.dp),
            imageVector = ImageVector.vectorResource(R.drawable.ic_camera_20),
            contentDescription = null,
            tint = HilingualTheme.colors.gray300
        )
    }
}

@Preview
@Composable
private fun PhotoSelectButtonPreview() {
    val context = LocalContext.current

    HilingualTheme {
        PhotoSelectButton(
            onClick = {
                Toast.makeText(context, "PhotoSelectButton Clicked", Toast.LENGTH_SHORT)
                    .show()
            }
        )
    }
}