package com.hilingual.data.widget.repositoryimpl

import com.hilingual.core.common.util.suspendRunCatching
import com.hilingual.core.common.util.toIsoDate
import com.hilingual.core.network.auth.TokenProvider
import com.hilingual.data.widget.datasource.WidgetLocalDataSource
import com.hilingual.data.widget.datasource.WidgetRemoteDataSource
import com.hilingual.data.widget.model.WidgetStreakModel
import com.hilingual.data.widget.model.WidgetTopicModel
import com.hilingual.data.widget.model.toModel
import com.hilingual.data.widget.repository.WidgetRepository
import java.time.LocalDate
import javax.inject.Inject

internal class WidgetRepositoryImpl @Inject constructor(
    private val widgetRemoteDataSource: WidgetRemoteDataSource,
    private val widgetLocalDataSource: WidgetLocalDataSource,
    private val tokenProvider: TokenProvider,
) : WidgetRepository {
    override suspend fun isLoggedIn(): Boolean =
        !tokenProvider.getAccessToken().isNullOrBlank()

    override suspend fun getTopic(date: LocalDate): Result<WidgetTopicModel> {
        val remoteResult = suspendRunCatching {
            widgetRemoteDataSource.getTopic(date = date.toIsoDate()).data!!.toModel()
        }

        remoteResult.getOrNull()?.let { topic ->
            suspendRunCatching { widgetLocalDataSource.saveTopic(topic) }
            return remoteResult
        }

        val cachedTopic = suspendRunCatching { widgetLocalDataSource.getTopic(date) }.getOrNull()
        return cachedTopic?.let(Result.Companion::success) ?: remoteResult
    }

    override suspend fun getStreak(date: LocalDate): Result<WidgetStreakModel> {
        val remoteResult = suspendRunCatching {
            check(isLoggedIn())
            widgetRemoteDataSource.getStreak(date = date.toIsoDate()).data!!.toModel()
        }

        remoteResult.getOrNull()?.let { streak ->
            suspendRunCatching { widgetLocalDataSource.saveStreak(streak) }
            return remoteResult
        }

        val cachedStreak = suspendRunCatching { widgetLocalDataSource.getStreak(date) }.getOrNull()
        return cachedStreak?.let(Result.Companion::success) ?: remoteResult
    }

    override suspend fun clearCache() {
        widgetLocalDataSource.clear()
    }
}
