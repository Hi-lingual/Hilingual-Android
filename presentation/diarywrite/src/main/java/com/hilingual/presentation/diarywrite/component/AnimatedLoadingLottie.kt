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

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.LottieComposition
import com.hilingual.core.designsystem.component.image.HilingualLottieAnimation
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun AnimatedLoadingLottie(
    lottieCompositions: ImmutableList<LottieComposition?>,
    height: Dp,
    modifier: Modifier = Modifier,
) {
    if (lottieCompositions.isEmpty()) return

    val fadeCycleState = rememberFadeCycleState(itemCount = lottieCompositions.size)

    HilingualLottieAnimation(
        modifier = modifier
            .width(200.dp)
            .height(height)
            .graphicsLayer {
                alpha = fadeCycleState.alpha
                clip = fadeCycleState.alpha != 1f
            },
        composition = lottieCompositions[fadeCycleState.currentIndex],
        isInfinite = true,
    )
}
