package com.hilingual.presentation.voca.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.common.extension.addFocusCleaner
import com.hilingual.core.designsystem.component.textfield.HilingualSearchTextField
import com.hilingual.core.designsystem.component.topappbar.TitleLeftAlignedTopAppBar
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
internal fun VocaHeader(
    searchText: () -> String,
    onSearchTextChanged: (String) -> Unit,
    onCloseButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = HilingualTheme.colors.hilingualBlack
            )
    ) {
        TitleLeftAlignedTopAppBar(
            title = "나의 단어장"
        )
        HilingualSearchTextField(
            value = searchText(),
            onValueChanged = onSearchTextChanged,
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 16.dp)
                .addFocusCleaner(focusManager),
            onTrailingIconClick = {
                onCloseButtonClick()
                focusManager.clearFocus()
            }
        )
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Preview
@Composable
private fun VocaHeaderPreview() {
    HilingualTheme {
        var searchText by remember { mutableStateOf("") }

        VocaHeader(
            searchText = { searchText },
            onSearchTextChanged = { searchText = it },
            onCloseButtonClick = { }
        )
    }
}

