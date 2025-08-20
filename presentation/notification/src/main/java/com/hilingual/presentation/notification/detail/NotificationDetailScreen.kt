package com.hilingual.presentation.notification.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.component.topappbar.BackTopAppBar
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.notification.detail.component.NotificationDetailContent

@Composable
internal fun NotificationDetailScreen(
    paddingValues: PaddingValues,
    onBackClick: () -> Unit,
    title: String,
    date: String,
    content: String,
    modifier: Modifier = Modifier.Companion
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(HilingualTheme.colors.white)
            .padding(paddingValues)
    ) {
        BackTopAppBar(
            title = "알림",
            onBackClicked = onBackClick
        )
        NotificationDetailContent(
            title = title,
            date = date,
            content = content
        )
    }
}

@Preview
@Composable
private fun NotificationDetailScreenPreview() {
    HilingualTheme {
        NotificationDetailScreen(
            paddingValues = PaddingValues(0.dp),
            onBackClick = {},
            title = "v 1.1.0 업데이트 알림",
            date = "2025.08.05",
            content = """
            안녕하세요. 하이링구얼 입니다. 

            하이링구얼 앱이 v.1.1.1 로 업데이트 되었습니다!

            [업데이트 내용]
            앱 안정화를 위한 관련 수정

            앱 최신 버전을 설치하여 새로워진 하이링구얼 앱을 사용해 보세요.
            
            감사합니다.
            
            [설치하러가기](https://youtu.be/tPbr41kRB-w?si=Cmlx2Up2rVvvrcA9)  
            """.trimIndent()
        )
    }
}
