package com.hilingual.data.widget.datasource

import com.hilingual.data.widget.model.WidgetStreakModel
import com.hilingual.data.widget.model.WidgetTopicModel

internal interface WidgetLocalDataSource {
    suspend fun getTopic(): WidgetTopicModel?

    suspend fun saveTopic(topic: WidgetTopicModel)

    suspend fun getStreak(): WidgetStreakModel?

    suspend fun saveStreak(streak: WidgetStreakModel)
}
