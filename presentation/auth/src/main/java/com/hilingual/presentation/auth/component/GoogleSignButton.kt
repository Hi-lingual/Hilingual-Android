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
package com.hilingual.presentation.auth.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.auth.R

@Composable
internal fun GoogleSignButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(HilingualTheme.colors.white)
            .noRippleClickable(onClick = onClick)
            .padding(vertical = 18.dp, horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_google_20),
            contentDescription = null,
            tint = Color.Unspecified
        )

        Text(
            text = "Google로 계속하기",
            style = HilingualTheme.typography.bodySB16,
            color = HilingualTheme.colors.hilingualBlack
        )

        Spacer(Modifier.width(20.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun GoogleSignButtonPreview() {
    HilingualTheme {
        GoogleSignButton(
            onClick = {}
        )
    }
}
