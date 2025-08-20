package com.hilingual.presentation.notification.main.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.R
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
internal fun EmptyImage(
    modifier: Modifier = Modifier
) {
    Column(
        modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.weight(0.5f))

        Image(
            painter = painterResource(R.drawable.img_diary_empty),
            contentDescription = null,
            modifier = Modifier.size(width = 200.dp, height = 100.dp)
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "아직 알림이 없어요.",
            style = HilingualTheme.typography.headM18,
            color = HilingualTheme.colors.gray500
        )

        Spacer(Modifier.weight(1f))
    }
}
