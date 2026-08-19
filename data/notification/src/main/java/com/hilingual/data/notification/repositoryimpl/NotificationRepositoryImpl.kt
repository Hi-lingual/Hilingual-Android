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
package com.hilingual.data.notification.repositoryimpl

import com.hilingual.core.common.util.suspendRunCatching
import com.hilingual.data.notification.datasource.NotificationLocalDataSource
import com.hilingual.data.notification.repository.NotificationRepository
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val notificationLocalDataSource: NotificationLocalDataSource,
) : NotificationRepository {
    override suspend fun getIsNotificationDialogShown(): Result<Boolean> =
        suspendRunCatching {
            notificationLocalDataSource.getIsNotificationDialogShown()
        }

    override suspend fun updateIsNotificationDialogShown(isShown: Boolean): Result<Unit> =
        suspendRunCatching {
            notificationLocalDataSource.updateIsNotificationDialogShown(isShown = isShown)
        }
}
