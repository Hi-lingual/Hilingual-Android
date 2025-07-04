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

val SuitBold = FontFamily(Font(R.font.suit_bold, FontWeight.Bold))
val SuitSemiBold = FontFamily(Font(R.font.suit_semibold, FontWeight.SemiBold))
val SuitMedium = FontFamily(Font(R.font.suit_medium, FontWeight.Medium))
val SuitRegular = FontFamily(Font(R.font.suit_regular, FontWeight.Normal))

@Stable
class HilingualTypography(
    headB20: TextStyle,
    headSB20: TextStyle,
    headB18: TextStyle,
    headM18: TextStyle,
    headB16: TextStyle,
    bodyM20: TextStyle,
    bodyR18: TextStyle,
    bodySB16: TextStyle,
    bodyM16: TextStyle,
    bodyR16: TextStyle,
    bodyB14: TextStyle,
    bodySB14: TextStyle,
    bodyM14: TextStyle,
    bodySB12: TextStyle,
    captionR14: TextStyle,
    captionM12: TextStyle,
    captionR12: TextStyle
) {
    var headB20 by mutableStateOf(headB20)
        private set
    var headSB20 by mutableStateOf(headSB20)
        private set
    var headB18 by mutableStateOf(headB18)
        private set
    var headM18 by mutableStateOf(headM18)
        private set
    var headB16 by mutableStateOf(headB16)
        private set
    var bodyM20 by mutableStateOf(bodyM20)
        private set
    var bodyR18 by mutableStateOf(bodyR18)
        private set
    var bodySB16 by mutableStateOf(bodySB16)
        private set
    var bodyM16 by mutableStateOf(bodyM16)
        private set
    var bodyR16 by mutableStateOf(bodyR16)
        private set
    var bodyB14 by mutableStateOf(bodyB14)
        private set
    var bodySB14 by mutableStateOf(bodySB14)
        private set
    var bodyM14 by mutableStateOf(bodyM14)
        private set
    var bodySB12 by mutableStateOf(bodySB12)
        private set
    var captionR14 by mutableStateOf(captionR14)
        private set
    var captionM12 by mutableStateOf(captionM12)
        private set
    var captionR12 by mutableStateOf(captionR12)
        private set

    fun copy(
        headB20: TextStyle = this.headB20,
        headSB20: TextStyle = this.headSB20,
        headB18: TextStyle = this.headB18,
        headM18: TextStyle = this.headM18,
        headB16: TextStyle = this.headB16,
        bodyM20: TextStyle = this.bodyM20,
        bodyR18: TextStyle = this.bodyR18,
        bodySB16: TextStyle = this.bodySB16,
        bodyM16: TextStyle = this.bodyM16,
        bodyR16: TextStyle = this.bodyR16,
        bodyB14: TextStyle = this.bodyB14,
        bodySB14: TextStyle = this.bodySB14,
        bodyM14: TextStyle = this.bodyM14,
        bodySB12: TextStyle = this.bodySB12,
        captionR14: TextStyle = this.captionR14,
        captionM12: TextStyle = this.captionM12,
        captionR12: TextStyle = this.captionR12
    ) = HilingualTypography(
        headB20,
        headSB20,
        headB18,
        headM18,
        headB16,
        bodyM20,
        bodyR18,
        bodySB16,
        bodyM16,
        bodyR16,
        bodyB14,
        bodySB14,
        bodyM14,
        bodySB12,
        captionR14,
        captionM12,
        captionR12
    )

    fun update(other: HilingualTypography) {
        headB20 = other.headB20
        headSB20 = other.headSB20
        headB18 = other.headB18
        headM18 = other.headM18
        headB16 = other.headB16
        bodyM20 = other.bodyM20
        bodyR18 = other.bodyR18
        bodySB16 = other.bodySB16
        bodyM16 = other.bodyM16
        bodyR16 = other.bodyR16
        bodyB14 = other.bodyB14
        bodySB14 = other.bodySB14
        bodyM14 = other.bodyM14
        bodySB12 = other.bodySB12
        captionR14 = other.captionR14
        captionM12 = other.captionM12
        captionR12 = other.captionR12
    }
}

@Composable
fun HilingualTypography(): HilingualTypography{
    return HilingualTypography(
        headB20 = TextStyle(
            fontFamily = SuitBold,
            fontSize = 20.sp
        ),
        headSB20 = TextStyle(
            fontFamily = SuitSemiBold,
            fontSize = 20.sp
        ),
        headB18 = TextStyle(
            fontFamily = SuitBold,
            fontSize = 18.sp
        ),
        headM18 = TextStyle(
            fontFamily = SuitMedium,
            fontSize = 18.sp
        ),
        headB16 = TextStyle(
            fontFamily = SuitBold,
            fontSize = 16.sp
        ),
        bodyM20 = TextStyle(
            fontFamily = SuitMedium,
            fontSize = 20.sp
        ),
        bodyR18 = TextStyle(
            fontFamily = SuitRegular,
            fontSize = 18.sp
        ),
        bodySB16 = TextStyle(
            fontFamily = SuitSemiBold,
            fontSize = 16.sp,
            lineHeight = 1.4.em,
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = LineHeightStyle.Trim.None
            )
        ),
        bodyM16 = TextStyle(
            fontFamily = SuitMedium,
            fontSize = 16.sp,
            lineHeight = 1.4.em,
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = LineHeightStyle.Trim.None
            )
        ),
        bodyR16 = TextStyle(
            fontFamily = SuitRegular,
            fontSize = 16.sp,
            lineHeight = 1.4.em,
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = LineHeightStyle.Trim.None
            )
        ),
        bodyB14 = TextStyle(
            fontFamily = SuitBold,
            fontSize = 14.sp
        ),
        bodySB14 = TextStyle(
            fontFamily = SuitSemiBold,
            fontSize = 14.sp
        ),
        bodyM14 = TextStyle(
            fontFamily = SuitMedium,
            fontSize = 14.sp
        ),
        bodySB12 = TextStyle(
            fontFamily = SuitSemiBold,
            fontSize = 12.sp
        ),
        captionR14 = TextStyle(
            fontFamily = SuitRegular,
            fontSize = 14.sp
        ),
        captionM12 = TextStyle(
            fontFamily = SuitMedium,
            fontSize = 12.sp
        ),
        captionR12 = TextStyle(
            fontFamily = SuitRegular,
            fontSize = 12.sp
        )
    )
}

@Preview(showBackground = true)
@Composable
fun HilingualTypographyPreview() {
    HilingualTheme {
        Column {
            Text(
                "HilingualTheme",
                style = HilingualTheme.typography.headB20
            )
            Text(
                "HilingualTheme",
                style = HilingualTheme.typography.headSB20
            )
            Text(
                "HilingualTheme",
                style = HilingualTheme.typography.headB18
            )
            Text(
                "HilingualTheme",
                style = HilingualTheme.typography.headM18
            )
            Text(
                "HilingualTheme",
                style = HilingualTheme.typography.headB16
            )
            Text(
                "HilingualTheme",
                style = HilingualTheme.typography.bodyM20
            )
            Text(
                "HilingualTheme",
                style = HilingualTheme.typography.bodyR18
            )
            Text(
                "HilingualTheme",
                style = HilingualTheme.typography.bodySB16
            )
            Text(
                "HilingualTheme",
                style = HilingualTheme.typography.bodyM16
            )
            Text(
                "HilingualTheme",
                style = HilingualTheme.typography.bodyR16
            )
            Text(
                "HilingualTheme",
                style = HilingualTheme.typography.bodyB14
            )
            Text(
                "HilingualTheme",
                style = HilingualTheme.typography.bodySB14
            )
            Text(
                "HilingualTheme",
                style = HilingualTheme.typography.bodyM14
            )
            Text(
                "HilingualTheme",
                style = HilingualTheme.typography.bodySB12
            )
            Text(
                "HilingualTheme",
                style = HilingualTheme.typography.captionR14
            )
            Text(
                "HilingualTheme",
                style = HilingualTheme.typography.captionM12
            )
            Text(
                "HilingualTheme",
                style = HilingualTheme.typography.captionR12
            )
        }
    }
}