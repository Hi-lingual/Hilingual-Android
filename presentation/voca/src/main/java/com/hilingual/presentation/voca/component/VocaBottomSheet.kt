package com.hilingual.presentation.voca.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.designsystem.component.bottomsheet.HilingualBasicBottomSheet
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.voca.R

@Composable
internal fun SortBottomSheetContent(
    selectedType: WordSortType,
    onTypeSelected: (WordSortType) -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "단어 정렬",
            style = HilingualTheme.typography.headB16,
            color = HilingualTheme.colors.hilingualBlack
        )

        Spacer(modifier = Modifier.height(24.dp))

        WordSortType.entries.forEach { type ->
            key(type) {
                val isSelected = type == selectedType
                val contentColor =
                    if (isSelected) HilingualTheme.colors.hilingualBlack else HilingualTheme.colors.gray400

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                        .noRippleClickable { onTypeSelected(type) },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = type.iconRes),
                        contentDescription = null,
                        tint = contentColor
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = type.text,
                        style = HilingualTheme.typography.bodySB14,
                        color = contentColor
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    if (isSelected) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_check_24),
                            contentDescription = null,
                            tint = HilingualTheme.colors.hilingualBlack
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun WordSortBottomSheet(
    selectedType: WordSortType,
    onDismiss: () -> Unit,
    onTypeSelected: (WordSortType) -> Unit
) {
    HilingualBasicBottomSheet(
        onDismiss = onDismiss
    ) {
        SortBottomSheetContent(
            selectedType = selectedType,
            onTypeSelected = {
                onTypeSelected(it)
                onDismiss()
            }
        )
    }
}
