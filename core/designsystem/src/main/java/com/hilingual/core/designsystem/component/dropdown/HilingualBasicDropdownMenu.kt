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
package com.hilingual.core.designsystem.component.dropdown

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.hilingual.core.common.extension.dropShadow
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.designsystem.R
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
fun HilingualBasicDropdownMenu(
    isExpanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    offsetY: Dp = 4.dp,
    content: @Composable () -> Unit
) {
    var iconHeight by remember { mutableIntStateOf(0) }
    val density = LocalDensity.current

    val popupYOffset = with(density) { offsetY.roundToPx() }

    Box {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_more_24),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier
                .onGloballyPositioned { coordinates ->
                    iconHeight = coordinates.size.height
                }
                .noRippleClickable(onClick = { onExpandedChange(!isExpanded) })
        )

        if (isExpanded) {
            Popup(
                alignment = Alignment.TopEnd,
                onDismissRequest = { onExpandedChange(false) },
                offset = IntOffset(x = 0, y = iconHeight + popupYOffset)
            ) {
                Column(
                    modifier = modifier
                        .dropShadow(
                            color = HilingualTheme.colors.black.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(size = 10.dp),
                            offsetX = 0.dp,
                            offsetY = 0.dp,
                            spread = 0.dp,
                            blur = 15.dp
                        )
                        .clip(RoundedCornerShape(size = 10.dp))
                        .background(color = Color.White)
                        .width(182.dp)
                ) {
                    content()
                }
            }
        }
    }
}

@Composable
fun HilingualDropdownMenuItem(
    text: String,
    @DrawableRes iconResId: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    textColor: Color = HilingualTheme.colors.gray700
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(12.dp)
            .fillMaxWidth()
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(iconResId),
            tint = Color.Unspecified,
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )

        Text(
            text = text,
            style = HilingualTheme.typography.bodySB14,
            color = textColor
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DropdownMenuPreview() {
    HilingualTheme {
        var expanded1 by remember { mutableStateOf(false) }
        var expanded2 by remember { mutableStateOf(false) }

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.End,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            HilingualBasicDropdownMenu( // Option1
                isExpanded = expanded1,
                onExpandedChange = { expanded1 = it }
            ) {
                HilingualDropdownMenuItem(
                    text = "비공개하기",
                    iconResId = R.drawable.ic_hide_24,
                    onClick = {
                        expanded1 = false
                    }
                )
            }

            HilingualBasicDropdownMenu( // Option2
                isExpanded = expanded2,
                onExpandedChange = { expanded2 = it }
            ) {
                HilingualDropdownMenuItem(
                    text = "비공개하기",
                    iconResId = R.drawable.ic_hide_24,
                    onClick = {
                        expanded2 = false
                    }
                )
                HorizontalDivider(
                    thickness = 1.dp,
                    color = HilingualTheme.colors.gray200
                )
                HilingualDropdownMenuItem(
                    text = "삭제하기",
                    iconResId = R.drawable.ic_delete_24,
                    onClick = {
                        expanded2 = false
                    },
                    textColor = HilingualTheme.colors.alertRed
                )
            }
        }
    }
}
