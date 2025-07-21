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
package com.hilingual.presentation.diarywrite.component

import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import androidx.compose.ui.unit.Dp

sealed class DiaryFeedbackState() {
    object Default : DiaryFeedbackState()
    object Loading : DiaryFeedbackState()
    data class Complete(val diaryId: Long) : DiaryFeedbackState()
    data class Failure(val throwable: Throwable) : DiaryFeedbackState()
}

sealed class FeedbackMedia {
    data class Lottie(
        @RawRes val resId: Int,
        val heightDp: Dp
    ) : FeedbackMedia()

    data class Image(
        @DrawableRes val resId: Int,
        val heightDp: Dp
    ) : FeedbackMedia()
}

data class FeedbackUIData(
    val title: String = "",
    val description: String? = null,
    val media: FeedbackMedia
)
