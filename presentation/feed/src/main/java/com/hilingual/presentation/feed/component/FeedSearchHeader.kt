package com.hilingual.presentation.feed.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.common.extension.addFocusCleaner
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.designsystem.R
import com.hilingual.core.designsystem.component.textfield.HilingualSearchTextField
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
internal fun FeedSearchHeader(
    searchText: () -> String,
    onSearchTextChanged: (String) -> Unit,
    onClearClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(HilingualTheme.colors.white)
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        Icon(
            modifier = Modifier
                .size(24.dp)
                .noRippleClickable(onClick = onBackClick),
            imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_left_24_back),
            contentDescription = null,
            tint = HilingualTheme.colors.black
        )

        Spacer(Modifier.width(8.dp))

        HilingualSearchTextField(
            value = searchText(),
            onValueChanged = onSearchTextChanged,
            placeholder = "닉네임을 입력해주세요.",
            backgroundColor = HilingualTheme.colors.gray100,
            modifier = Modifier
                .weight(1f)
                .addFocusCleaner(focusManager),
            onTrailingIconClick = {
                onClearClick()
                focusManager.clearFocus()
            },
            onSearchAction = { focusManager.clearFocus() }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FeedSearchHeaderPreview() {
    HilingualTheme {
        var searchText by remember { mutableStateOf("") }

        FeedSearchHeader(
            searchText = { searchText },
            onSearchTextChanged = { searchText = it },
            onClearClick = {},
            onBackClick = {}
        )
    }
}