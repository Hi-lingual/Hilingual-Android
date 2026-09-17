/*
 * Copyright 2026 The Hilingual Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hilingual.data.widget.repositoryimpl

import android.os.SystemClock
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
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal class WidgetRepositoryImpl @Inject constructor(
    private val widgetRemoteDataSource: WidgetRemoteDataSource,
    private val widgetLocalDataSource: WidgetLocalDataSource,
    private val tokenProvider: TokenProvider,
) : WidgetRepository {
    private val topicMutex = Mutex()
    private val streakMutex = Mutex()
    private var recentTopicRequest: RecentRequest<WidgetTopicModel>? = null
    private var recentStreakRequest: RecentRequest<WidgetStreakModel>? = null

    override suspend fun isLoggedIn(): Boolean =
        !tokenProvider.getAccessToken().isNullOrBlank()

    override suspend fun getTopic(date: LocalDate): Result<WidgetTopicModel> =
        topicMutex.withLock {
            recentTopicRequest
                ?.takeIf { request -> request.canReuse(date) }
                ?.let { request -> return@withLock request.result }

            loadTopic(date).also { result ->
                recentTopicRequest = RecentRequest(date, result)
            }
        }

    private suspend fun loadTopic(date: LocalDate): Result<WidgetTopicModel> {
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

    override suspend fun getStreak(date: LocalDate): Result<WidgetStreakModel> =
        streakMutex.withLock {
            recentStreakRequest
                ?.takeIf { request -> request.canReuse(date) }
                ?.let { request -> return@withLock request.result }

            loadStreak(date).also { result ->
                recentStreakRequest = RecentRequest(date, result)
            }
        }

    private suspend fun loadStreak(date: LocalDate): Result<WidgetStreakModel> {
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
        topicMutex.withLock {
            streakMutex.withLock {
                recentTopicRequest = null
                recentStreakRequest = null
                widgetLocalDataSource.clear()
            }
        }
    }

    private data class RecentRequest<T>(
        val date: LocalDate,
        val result: Result<T>,
        val completedAtMillis: Long = SystemClock.elapsedRealtime(),
    ) {
        fun canReuse(requestDate: LocalDate): Boolean =
            date == requestDate &&
                SystemClock.elapsedRealtime() - completedAtMillis <= REQUEST_DEDUPLICATION_WINDOW_MILLIS
    }

    private companion object {
        const val REQUEST_DEDUPLICATION_WINDOW_MILLIS = 2_000L
    }
}
