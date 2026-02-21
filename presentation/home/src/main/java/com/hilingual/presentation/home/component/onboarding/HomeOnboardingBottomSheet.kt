package com.hilingual.presentation.home.component.onboarding

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.designsystem.R
import com.hilingual.core.designsystem.component.bottomsheet.HilingualBasicBottomSheet
import com.hilingual.core.designsystem.theme.HilingualTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeOnboardingBottomSheet(
    isVisible: Boolean,
    onCloseButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    HilingualBasicBottomSheet(
        isVisible = isVisible,
        onDismiss = {},
        properties = ModalBottomSheetProperties(
            shouldDismissOnClickOutside = false
        ),
        isDimEnabled = true,
        sheetGesturesEnabled = false,
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_close_24),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 12.dp)
                    .padding(horizontal = 16.dp)
                    .noRippleClickable(onClick = onCloseButtonClick)
                    .padding(10.dp)
                    .align(Alignment.TopEnd)
            )
            content()
        }
    }
}

@Preview
@Composable
private fun HomeOnboardingBottomSheetPreview() {
    HilingualTheme {
        HomeOnboardingBottomSheet(
            isVisible = true,
            onCloseButtonClick = { }
        ) { }
    }
}
