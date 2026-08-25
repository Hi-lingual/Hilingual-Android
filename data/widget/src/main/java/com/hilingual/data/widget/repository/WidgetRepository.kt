package com.hilingual.data.widget.repository

import com.hilingual.data.widget.model.WidgetStreakModel
import com.hilingual.data.widget.model.WidgetTopicModel
import java.time.LocalDate

interface WidgetRepository {
    suspend fun isLoggedIn(): Boolean

    suspend fun getTopic(date: LocalDate): Result<WidgetTopicModel>

    suspend fun getStreak(date: LocalDate): Result<WidgetStreakModel>

    suspend fun clearCache()
}
