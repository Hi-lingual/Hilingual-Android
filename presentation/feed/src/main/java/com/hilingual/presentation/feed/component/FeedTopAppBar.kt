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
package com.hilingual.presentation.feed.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.designsystem.R
import com.hilingual.core.designsystem.component.image.NetworkImage
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
internal fun FeedTopAppBar(
    profileImageUrl: String?,
    onProfileClick: () -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 9.dp, horizontal = 16.dp)
    ) {
        Text(
            text = "피드",
            style = HilingualTheme.typography.headB18,
            color = HilingualTheme.colors.hilingualBlack
        )

        Spacer(Modifier.weight(1f))

        Box(
            modifier = Modifier
                .noRippleClickable(onClick = onSearchClick)
                .padding(6.dp)
                .size(36.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_search_20),
                contentDescription = null,
                tint = HilingualTheme.colors.gray400,
                modifier = Modifier
                    .noRippleClickable(onClick = onSearchClick)
                    .size(24.dp)
                    .align(Alignment.Center)
            )
        }

        Spacer(Modifier.width(8.dp))

        NetworkImage(
            imageUrl = profileImageUrl,
            contentDescription = null,
            modifier = Modifier
                .noRippleClickable(onClick = onProfileClick)
                .size(36.dp)
                .border(
                    width = 1.dp,
                    color = HilingualTheme.colors.gray400,
                    shape = CircleShape
                )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FeedTopAppBarPreview() {
    HilingualTheme {
        FeedTopAppBar(
            profileImageUrl = "",
            onProfileClick = {},
            onSearchClick = {}
        )
    }
}
