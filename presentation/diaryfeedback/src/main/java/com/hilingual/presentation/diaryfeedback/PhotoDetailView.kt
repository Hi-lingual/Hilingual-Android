package com.hilingual.presentation.diaryfeedback

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import com.hilingual.core.designsystem.component.image.NetworkImage
import com.hilingual.core.designsystem.component.topappbar.CloseOnlyTopAppBar
import com.hilingual.core.designsystem.theme.HilingualTheme

//TODO: sharedTransition 사용
@Composable
internal fun PhotoDetailView(
    imageUrl: String,
    onClickBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(HilingualTheme.colors.black)
    ) {
        CloseOnlyTopAppBar(
            onCloseClicked = onClickBack,
            modifier = Modifier.align(Alignment.TopCenter)
        )

        NetworkImage(
            imageUrl = imageUrl,
            shape = RectangleShape,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PhotoDetailPreview() {
    HilingualTheme {
        PhotoDetailView(
            imageUrl = "",
            onClickBack = {}
        )
    }
}