package com.hilingual.presentation.feedprofile.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.component.dialog.diary.DiaryUnpublishDialog
import com.hilingual.core.designsystem.component.dropdown.HilingualBasicDropdownMenu
import com.hilingual.core.designsystem.component.dropdown.HilingualDropdownMenuItem
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.core.designsystem.R as DesignSystemR

@Composable
internal fun FeedReportDropDownMenu(
    isExpanded: Boolean,
    isMine: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onUnpublishClick: () -> Unit,
    onReportClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var publishDialogVisible by remember { mutableStateOf(false) }

    HilingualBasicDropdownMenu(
        isExpanded = isExpanded,
        onExpandedChange = onExpandedChange,
        modifier = modifier
    ) {
        HilingualDropdownMenuItem(
            text = if (isMine) "비공개하기" else "신고하기",
            iconResId = if (isMine) DesignSystemR.drawable.ic_hide_24 else DesignSystemR.drawable.ic_report_24,
            onClick = {
                if (isMine) {
                    publishDialogVisible = true
                } else {
                    onReportClick()
                }
                onExpandedChange(false)
            }
        )
    }

    if (publishDialogVisible && isMine) {
        DiaryUnpublishDialog(
            isVisible = true,
            onDismiss = { publishDialogVisible = false },
            onPrivateClick = {
                onUnpublishClick()
                publishDialogVisible = false
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FeedReportDropDownMenuPreview() {
    var isExpanded by remember { mutableStateOf(false) }
    var isExpanded1 by remember { mutableStateOf(false) }
    HilingualTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FeedReportDropDownMenu(
                isExpanded = isExpanded,
                isMine = true,
                onExpandedChange = { isExpanded = it },
                onUnpublishClick = {},
                onReportClick = {}
            )
            FeedReportDropDownMenu(
                isExpanded = isExpanded1,
                isMine = false,
                onExpandedChange = { isExpanded1 = it },
                onUnpublishClick = {},
                onReportClick = {}
            )
        }
    }
}
