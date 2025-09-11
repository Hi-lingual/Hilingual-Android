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
package com.hilingual.presentation.voca.component

import android.view.Gravity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.designsystem.component.tag.WordPhraseTypeTag
import com.hilingual.core.designsystem.theme.HilingualTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import com.hilingual.core.designsystem.R as DesignSystemR

@Composable
internal fun VocaDialog(
    onDismiss: () -> Unit,
    phraseId: Long,
    phrase: String,
    phraseType: ImmutableList<String>,
    explanation: String,
    writtenDate: String,
    isBookmarked: Boolean,
    onBookmarkClick: (Long, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    properties: DialogProperties = DialogProperties(
        usePlatformDefaultWidth = false,
        decorFitsSystemWindows = false,
    )
) {
    var isMarked by remember { mutableStateOf(isBookmarked) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = properties
    ) {
        val dialogWindowProvider = LocalView.current.parent as? DialogWindowProvider
        dialogWindowProvider?.window?.setGravity(Gravity.BOTTOM)

        Box(
            modifier = modifier
                .fillMaxSize()
                .background(HilingualTheme.colors.dim2)
                .padding(horizontal = 16.dp)
                .padding(bottom = 20.dp)
                .noRippleClickable(onClick = onDismiss),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .fillMaxWidth()
                    .background(
                        color = HilingualTheme.colors.white,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(top = 28.dp, bottom = 40.dp)
                    .padding(horizontal = 24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(bottom = 80.dp, end = 44.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        phraseType.forEach { type ->
                            key(type) {
                                WordPhraseTypeTag(phraseType = type)
                            }
                        }
                    }

                    Text(
                        text = phrase,
                        style = HilingualTheme.typography.bodyM20,
                        color = HilingualTheme.colors.black
                    )

                    Text(
                        text = explanation,
                        style = HilingualTheme.typography.bodyM14,
                        color = HilingualTheme.colors.black
                    )
                }

                Icon(
                    imageVector = ImageVector.vectorResource(
                        id = if (isMarked) {
                            DesignSystemR.drawable.ic_save_28_filled
                        } else {
                            DesignSystemR.drawable.ic_save_28_empty
                        }
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .size(36.dp)
                        .align(Alignment.TopEnd)
                        .noRippleClickable {
                            isMarked = !isMarked
                            onBookmarkClick(phraseId, isMarked)
                        },
                    tint = Color.Unspecified
                )

                Text(
                    text = writtenDate,
                    style = HilingualTheme.typography.captionM12,
                    color = HilingualTheme.colors.gray400,
                    modifier = Modifier.align(Alignment.BottomEnd)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun VocaDialogPreview() {
    HilingualTheme {
        VocaDialog(
            onDismiss = {},
            phraseId = 1L,
            phrase = "take a rain check",
            phraseType = persistentListOf("동사", "숙어"),
            explanation = "다음 기회로 미루다, 나중에 하자고 하다",
            writtenDate = "2024.03.15",
            isBookmarked = true,
            onBookmarkClick = { _, _ -> }
        )
    }
}
