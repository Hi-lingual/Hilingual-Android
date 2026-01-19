/*
 * Copyright 2025 The Hilingual Project
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
package com.hilingual.presentation.home

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Stable
class HomeState(
    val scrollState: ScrollState
) {
    var isDiaryContinueDialogVisible by mutableStateOf(false)
        private set

    var isErrorDialogVisible by mutableStateOf(false)
        private set

    var isMoreMenuExpanded by mutableStateOf(false)
        private set

    var onErrorRetry: (() -> Unit)? = null
        private set

    fun showDiaryContinueDialog() {
        isDiaryContinueDialogVisible = true
    }

    fun hideDiaryContinueDialog() {
        isDiaryContinueDialogVisible = false
    }

    fun showErrorDialog(onRetry: () -> Unit) {
        onErrorRetry = onRetry
        isErrorDialogVisible = true
    }

    fun hideErrorDialog() {
        isErrorDialogVisible = false
        onErrorRetry = null
    }

    fun showMoreMenu() {
        isMoreMenuExpanded = true
    }

    fun hideMoreMenu() {
        isMoreMenuExpanded = false
    }
}

@Composable
fun rememberHomeState(
    scrollState: ScrollState = rememberScrollState()
): HomeState {
    return remember(scrollState) {
        HomeState(
            scrollState = scrollState
        )
    }
}
