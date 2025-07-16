package com.hilingual.core.designsystem.component.button

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.common.extension.dropShadow
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.designsystem.R
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.core.designsystem.theme.white

@Composable
fun BoxScope.HilingualFloatingButton(
    onClick: () -> Unit,
    isVisible: Boolean,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(
            initialOffsetY = { it },
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )
        ),
        exit = slideOutVertically(
            targetOffsetY = { it * 3 },
            animationSpec = tween(durationMillis = 100)
        ),
        modifier = modifier
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_up_fab_24),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier
                .noRippleClickable(onClick = onClick)
                .dropShadow(
                    shape = CircleShape,
                    color = HilingualTheme.colors.black.copy(alpha = 0.2f),
                    offsetX = 0.dp,
                    offsetY = 2.dp,
                    blur = 4.dp,
                    spread = 0.dp

                )
                .size(40.dp)
                .background(color = HilingualTheme.colors.white, shape = CircleShape)
                .padding(8.dp)
        )
    }
}

@Preview
@Composable
private fun HilingualFloatingButtonPreview() {
    HilingualTheme {
        Box(
            modifier = Modifier
                .background(white)
                .padding(40.dp)
        ) {
            HilingualFloatingButton(
                onClick = {},
                isVisible = true
            )
        }
    }
}
