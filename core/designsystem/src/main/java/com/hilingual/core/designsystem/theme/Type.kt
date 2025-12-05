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
package com.hilingual.core.designsystem.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.hilingual.core.designsystem.R

val PretendardSemiBold = FontFamily(Font(R.font.pretendard_semibold, FontWeight.SemiBold))
val PretendardMedium = FontFamily(Font(R.font.pretendard_medium, FontWeight.Medium))
val PretendardRegular = FontFamily(Font(R.font.pretendard_regular, FontWeight.Normal))

@Stable
class HilingualTypography(
    headSB20: TextStyle,
    headM20: TextStyle,
    headR20: TextStyle,
    headSB18: TextStyle,
    headM18: TextStyle,
    headR18: TextStyle,
    headSB16: TextStyle,
    bodyR17: TextStyle,
    bodyM16: TextStyle,
    bodyR16: TextStyle,
    bodyM15: TextStyle,
    bodyR15: TextStyle,
    bodySB14: TextStyle,
    bodyM14: TextStyle,
    bodyR14: TextStyle,
    bodyM12: TextStyle,
    captionR12: TextStyle
) {
    var headSB20 by mutableStateOf(headSB20)
        private set
    var headM20 by mutableStateOf(headM20)
        private set
    var headR20 by mutableStateOf(headR20)
        private set
    var headSB18 by mutableStateOf(headSB18)
        private set
    var headM18 by mutableStateOf(headM18)
        private set
    var headR18 by mutableStateOf(headR18)
        private set
    var headSB16 by mutableStateOf(headSB16)
        private set
    var bodyR17 by mutableStateOf(bodyR17)
        private set
    var bodyM16 by mutableStateOf(bodyM16)
        private set
    var bodyR16 by mutableStateOf(bodyR16)
        private set
    var bodyM15 by mutableStateOf(bodyM15)
        private set
    var bodyR15 by mutableStateOf(bodyR15)
        private set
    var bodySB14 by mutableStateOf(bodySB14)
        private set
    var bodyM14 by mutableStateOf(bodyM14)
        private set
    var bodyR14 by mutableStateOf(bodyR14)
        private set
    var bodyM12 by mutableStateOf(bodyM12)
        private set
    var captionR12 by mutableStateOf(captionR12)
        private set

    fun copy(
        headSB20: TextStyle = this.headSB20,
        headM20: TextStyle = this.headM20,
        headR20: TextStyle = this.headR20,
        headSB18: TextStyle = this.headSB18,
        headM18: TextStyle = this.headM18,
        headR18: TextStyle = this.headR18,
        headSB16: TextStyle = this.headSB16,
        bodyR17: TextStyle = this.bodyR17,
        bodyM16: TextStyle = this.bodyM16,
        bodyR16: TextStyle = this.bodyR16,
        bodyM15: TextStyle = this.bodyM15,
        bodyR15: TextStyle = this.bodyR15,
        bodySB14: TextStyle = this.bodySB14,
        bodyM14: TextStyle = this.bodyM14,
        bodyR14: TextStyle = this.bodyR14,
        bodyM12: TextStyle = this.bodyM12,
        captionR12: TextStyle = this.captionR12
    ) = HilingualTypography(
        headSB20,
        headM20,
        headR20,
        headSB18,
        headM18,
        headR18,
        headSB16,
        bodyR17,
        bodyM16,
        bodyR16,
        bodyM15,
        bodyR15,
        bodySB14,
        bodyM14,
        bodyR14,
        bodyM12,
        captionR12
    )

    fun update(other: HilingualTypography) {
        headSB20 = other.headSB20
        headM20 = other.headM20
        headR20 = other.headR20
        headSB18 = other.headSB18
        headM18 = other.headM18
        headR18 = other.headR18
        headSB16 = other.headSB16

        bodyR17 = other.bodyR17
        bodyM16 = other.bodyM16
        bodyR16 = other.bodyR16
        bodyM15 = other.bodyM15
        bodyR15 = other.bodyR15
        bodySB14 = other.bodySB14
        bodyM14 = other.bodyM14
        bodyR14 = other.bodyR14
        bodyM12 = other.bodyM12

        captionR12 = other.captionR12
    }
}

@Composable
fun HilingualTypography(): HilingualTypography {
    return HilingualTypography(
        headSB20 = TextStyle(
            fontFamily = PretendardSemiBold,
            fontSize = 20.sp
        ),
        headM20 = TextStyle(
            fontFamily = PretendardMedium,
            fontSize = 20.sp
        ),
        headR20 = TextStyle(
            fontFamily = PretendardRegular,
            fontSize = 20.sp
        ),
        headSB18 = TextStyle(
            fontFamily = PretendardSemiBold,
            fontSize = 18.sp,
            letterSpacing = 0.03.em
        ),
        headM18 = TextStyle(
            fontFamily = PretendardMedium,
            fontSize = 18.sp
        ),
        headR18 = TextStyle(
            fontFamily = PretendardRegular,
            fontSize = 18.sp
        ),
        headSB16 = TextStyle(
            fontFamily = PretendardSemiBold,
            fontSize = 16.sp
        ),
        bodyR17 = TextStyle(
            fontFamily = PretendardRegular,
            fontSize = 17.sp
        ),
        bodyM16 = TextStyle(
            fontFamily = PretendardMedium,
            fontSize = 16.sp,
            lineHeight = 1.4.em,
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = LineHeightStyle.Trim.None
            )
        ),
        bodyR16 = TextStyle(
            fontFamily = PretendardRegular,
            fontSize = 16.sp,
            lineHeight = 1.4.em,
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = LineHeightStyle.Trim.None
            )
        ),
        bodyM15 = TextStyle(
            fontFamily = PretendardMedium,
            fontSize = 15.sp,
            lineHeight = 1.4.em,
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = LineHeightStyle.Trim.None
            ),
            letterSpacing = 0.01.em
        ),
        bodyR15 = TextStyle(
            fontFamily = PretendardRegular,
            fontSize = 15.sp,
            lineHeight = 1.4.em,
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = LineHeightStyle.Trim.None
            ),
            letterSpacing = 0.01.em
        ),
        bodySB14 = TextStyle(
            fontFamily = PretendardSemiBold,
            fontSize = 14.sp
        ),
        bodyM14 = TextStyle(
            fontFamily = PretendardMedium,
            fontSize = 14.sp
        ),
        bodyR14 = TextStyle(
            fontFamily = PretendardRegular,
            fontSize = 14.sp
        ),
        bodyM12 = TextStyle(
            fontFamily = PretendardMedium,
            fontSize = 12.sp
        ),
        captionR12 = TextStyle(
            fontFamily = PretendardRegular,
            fontSize = 12.sp
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun HilingualTypographyPreview() {
    HilingualTheme {
        Column {
            Text(
                text = "HilingualTheme",
                style = HilingualTheme.typography.headSB20
            )
            Text(
                text = "HilingualTheme",
                style = HilingualTheme.typography.headM20
            )
            Text(
                text = "HilingualTheme",
                style = HilingualTheme.typography.headSB18
            )
            Text(
                text = "HilingualTheme",
                style = HilingualTheme.typography.headR18
            )
            Text(
                text = "HilingualTheme",
                style = HilingualTheme.typography.headSB16
            )
            Text(
                text = "HilingualTheme",
                style = HilingualTheme.typography.headR20
            )
            Text(
                text = "HilingualTheme",
                style = HilingualTheme.typography.headR18
            )
            Text(
                text = "HilingualTheme",
                style = HilingualTheme.typography.bodyM16
            )
            Text(
                text = "HilingualTheme",
                style = HilingualTheme.typography.bodyR16
            )
            Text(
                text = "HilingualTheme",
                style = HilingualTheme.typography.bodyR16
            )
            Text(
                text = "HilingualTheme",
                style = HilingualTheme.typography.bodySB14
            )
            Text(
                text = "HilingualTheme",
                style = HilingualTheme.typography.bodyM14
            )
            Text(
                text = "HilingualTheme",
                style = HilingualTheme.typography.bodyR14
            )
            Text(
                text = "HilingualTheme",
                style = HilingualTheme.typography.bodyM12
            )
            Text(
                text = "HilingualTheme",
                style = HilingualTheme.typography.bodyR14
            )
            Text(
                text = "HilingualTheme",
                style = HilingualTheme.typography.captionR12
            )
            Text(
                text = "HilingualTheme",
                style = HilingualTheme.typography.captionR12
            )
        }
    }
}
