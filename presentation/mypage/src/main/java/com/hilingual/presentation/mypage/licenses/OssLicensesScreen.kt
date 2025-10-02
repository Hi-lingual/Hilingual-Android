package com.hilingual.presentation.mypage.licenses

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.hilingual.core.designsystem.component.topappbar.BackTopAppBar
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.mikepenz.aboutlibraries.ui.compose.android.produceLibraries
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer

@Composable
fun OssLicensesScreen(
    paddingValues: PaddingValues,
    onBackClick: () -> Unit
) {
    val libraries by produceLibraries()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(HilingualTheme.colors.white)
            .padding(paddingValues)
    ) {
        BackTopAppBar(
            title = "오픈소스 라이선스",
            onBackClicked = onBackClick
        )
        LibrariesContainer(
            libraries = libraries,
            modifier = Modifier.fillMaxSize()
        )
    }
}
