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
package com.hilingual.presentation.community

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hilingual.core.common.extension.collectSideEffect
import com.hilingual.core.common.model.SnackbarRequest
import com.hilingual.core.common.trigger.LocalSnackbarTrigger
import com.hilingual.core.common.trigger.LocalToastTrigger
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
internal fun CommunityScreen(
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
    viewModel: CommunityViewModel = hiltViewModel()
) {
    val snackbarTrigger = LocalSnackbarTrigger.current
    val toastTrigger = LocalToastTrigger.current

    viewModel.event.collectSideEffect { event ->
        when (event) {
            is CommunityUiEvent.ShowSnackbar -> {
                snackbarTrigger(
                    SnackbarRequest(
                        message = event.message,
                        buttonText = event.actionLabel,
                        onClick = { toastTrigger("${event.actionLabel} 클릭됨") }
                    )
                )
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(HilingualTheme.colors.gray100),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = viewModel::showSnackbar1) {
            Text(text = "스낵바 1 띄우기")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = viewModel::showSnackbar2) {
            Text(text = "스낵바 2 띄우기")
        }
    }
}
