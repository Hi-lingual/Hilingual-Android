package com.hilingual.presentation.notification.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.hilingual.presentation.notification.navigation.NotificationDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
internal class NotificationDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val noticeId: Long = savedStateHandle.toRoute<NotificationDetail>().noticeId

    private val _uiState = MutableStateFlow(NotificationDetailUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadNoticeDetail()
    }

    private fun loadNoticeDetail() {
        _uiState.update {
            it.copy(
                noticeDetailTitle = "v 1.1.0 업데이트 알림 (ID: $noticeId)",
                noticeDetailDate = "2025.08.05",
                noticeDetailContent = """
                    안녕하세요. 하이링구얼 입니다.
        
                    하이링구얼 앱이 v.1.1.1 로 업데이트 되었습니다!
        
                    [업데이트 내용]
                    앱 안정화를 위한 관련 수정
        
                    앱 최신 버전을 설치하여 새로워진 하이링구얼 앱을 사용해 보세요.
        
                    감사합니다.
        
                    [설치하러가기](https://hilingual.com)
                """.trimIndent()
            )
        }
    }
}
