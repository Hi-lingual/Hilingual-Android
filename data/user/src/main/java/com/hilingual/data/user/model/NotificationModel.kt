package com.hilingual.data.user.model

sealed interface NotificationModel {
    val noticeId: Long
    val title: String
    val isRead: Boolean
    val publishedAt: String
}
