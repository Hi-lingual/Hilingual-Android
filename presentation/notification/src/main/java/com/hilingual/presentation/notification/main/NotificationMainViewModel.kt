package com.hilingual.presentation.notification.main

import androidx.lifecycle.ViewModel
import com.hilingual.presentation.notification.main.model.FeedNotificationItemModel
import com.hilingual.presentation.notification.main.model.NoticeNotificationItemModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
internal class NotificationMainViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(NotificationMainUiState())
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.update {
            it.copy(
                feedNotifications = persistentListOf(
                    FeedNotificationItemModel(
                        id = 0,
                        title = "방금 이병건님이 당신의 8월 14일 일기에 공감했습니다.",
                        date = "2025.08.15",
                        isRead = false,
                        deepLink = ""
                    ),
                    FeedNotificationItemModel(
                        id = 1,
                        title = "토착왜구맨님이 당신의 8월 14일 일기에 공감했습니다.",
                        date = "2025.08.15",
                        isRead = true,
                        deepLink = ""
                    )
                ),
                noticeNotifications = persistentListOf(
                    NoticeNotificationItemModel(
                        id = 0,
                        title = "v.1.1.0 업데이트 알림",
                        date = "2025.08.15",
                        isRead = false
                    ),
                    NoticeNotificationItemModel(
                        id = 1,
                        title = "시스템 점검에 따른 사용 중단 안내",
                        date = "2025.08.15",
                        isRead = true
                    )
                )
            )
        }
    }
}
