package com.hilingual.presentation.community

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.core.designsystem.R as DesignSystemR

@Composable
internal fun CommunityScreen(
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(HilingualTheme.colors.gray100),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.weight(0.35f))
        Image(
            painter = painterResource(DesignSystemR.drawable.img_prototype),
            contentDescription = null
        )
        Spacer(Modifier.weight(0.65f))
    }
}
