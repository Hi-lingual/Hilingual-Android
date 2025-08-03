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
package com.hilingual.core.designsystem.component.bottomsheet

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.theme.HilingualTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HilingualBasicBottomSheet(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(),
    isDimEnabled: Boolean = true,
    dragHandle: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = modifier,
        sheetState = sheetState,
        containerColor = HilingualTheme.colors.white,
        scrimColor = if (isDimEnabled) HilingualTheme.colors.dim1 else Color.Transparent,
        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
        dragHandle = dragHandle
    ) {
        content()
    }
}
