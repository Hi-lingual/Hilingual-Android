package com.hilingual.presentation.feedprofile.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hilingual.core.designsystem.component.bottomsheet.HilingualMenuBottomSheet
import com.hilingual.core.designsystem.component.bottomsheet.HilingualMenuBottomSheetItem
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.core.designsystem.R as DesignSystemR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ReportBlockBottomSheet(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onReportClick: () -> Unit,
    onBlockClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    HilingualMenuBottomSheet(
        isVisible = isVisible,
        onDismiss = onDismiss,
        modifier = modifier
    ) {
        HilingualMenuBottomSheetItem(
            text = "신고하기",
            iconResId = DesignSystemR.drawable.ic_block_24_gray,
            onClick = onReportClick
        )
        HilingualMenuBottomSheetItem(
            text = "차단하기",
            iconResId = DesignSystemR.drawable.ic_report_24,
            onClick = onBlockClick
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun ReportBlockBottomSheetPreviewVisible() {
    HilingualTheme {
        var isSheetVisible by remember { mutableStateOf(true) }

        ReportBlockBottomSheet(
            isVisible = isSheetVisible,
            onDismiss = { isSheetVisible = false },
            onReportClick = { },
            onBlockClick = { }
        )
    }
}
