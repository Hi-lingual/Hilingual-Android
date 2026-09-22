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
package com.hilingual.core.common.trigger

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DialogTriggerTest {
    @Test
    fun `기본 재연결 정책은 다이얼로그를 복원한다`() {
        var actualPolicy: DialogReconnectPolicy? = null
        val trigger = DialogTrigger { _, reconnectPolicy, _ ->
            actualPolicy = reconnectPolicy
        }

        trigger.show(onClick = {})

        assertEquals(DialogReconnectPolicy.RESTORE, actualPolicy)
    }

    @Test
    fun `명시한 재연결 정책을 다이얼로그 요청에 전달한다`() {
        var actualPolicy: DialogReconnectPolicy? = null
        val trigger = DialogTrigger { _, reconnectPolicy, _ ->
            actualPolicy = reconnectPolicy
        }

        trigger.show(
            reconnectPolicy = DialogReconnectPolicy.REPLACED_BY_RETRY,
            onClick = {},
        )

        assertEquals(DialogReconnectPolicy.REPLACED_BY_RETRY, actualPolicy)
    }

    @Test
    fun `재연결 재시도로 대체된 요청만 복원하지 않는다`() {
        assertTrue(DialogReconnectPolicy.RESTORE.shouldRestoreAfterReconnect)
        assertFalse(DialogReconnectPolicy.REPLACED_BY_RETRY.shouldRestoreAfterReconnect)
    }
}
