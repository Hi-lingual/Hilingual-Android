package com.hilingual.presentation.diaryfeedback.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.designsystem.component.bottomsheet.HilingualBasicBottomSheet
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.diaryfeedback.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FeedbackReportBottomSheet(
    onDismiss: () -> Unit,
    onReportClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    HilingualBasicBottomSheet(
        onDismiss = onDismiss,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp, horizontal = 16.dp)
        ) {
            Text(
                text = "AI 피드백",
                style = HilingualTheme.typography.headB16,
                color = HilingualTheme.colors.black,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(Modifier.height(24.dp))
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth()
                    .noRippleClickable(
                        onClick = onReportClick
                    )
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_report_24),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .size(24.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "신고하기",
                    style = HilingualTheme.typography.bodySB14,
                    color = HilingualTheme.colors.gray700
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FeedbackReportPreview() {
    HilingualTheme {
        var bottomSheetVisibility by remember { mutableStateOf(false) }

        if (bottomSheetVisibility) {
            FeedbackReportBottomSheet(
                onDismiss = { bottomSheetVisibility = false },
                onReportClick = {}
            )
        }

        Column(
            modifier = Modifier
                .background(HilingualTheme.colors.white)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "바텀시트를 띄워보아요",
                style = HilingualTheme.typography.bodyB14,
                modifier = Modifier.noRippleClickable(
                    onClick = { bottomSheetVisibility = true }
                )
            )
        }
    }
}
