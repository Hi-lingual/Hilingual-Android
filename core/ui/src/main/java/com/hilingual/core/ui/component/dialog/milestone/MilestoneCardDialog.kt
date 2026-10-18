package com.hilingual.core.ui.component.dialog.milestone

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.common.model.HilingualMessage
import com.hilingual.core.common.model.Milestone
import com.hilingual.core.common.trigger.LocalMessageController
import com.hilingual.core.designsystem.R
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.core.ui.util.rememberImageExporter

private val EXPORT_IMAGE_SIZE = DpSize(width = 360.dp, height = 640.dp)

private enum class MilestoneCardStyle(
    @DrawableRes val illustrationRes: Int,
    val messagePrefix: String,
    val countUnit: String,
    val messageSuffix: String,
) {
    REGULAR(
        illustrationRes = R.drawable.img_milestone_blue,
        messagePrefix = "Way to go! You've written ",
        countUnit = "stories",
        messageSuffix = " so far.\nBuild your own English diary with Hilingual.",
    ),
    HUNDRED_UNIT(
        illustrationRes = R.drawable.img_milestone_orange,
        messagePrefix = "WOW! You've written ",
        countUnit = "stories",
        messageSuffix = " so far.\nKeep up the great work with Hilingual!",
    ),
    ;

    val pointColor: Color
        @Composable get() = when (this) {
            REGULAR -> HilingualTheme.colors.hilingualBlue
            HUNDRED_UNIT -> HilingualTheme.colors.hilingualOrange
        }

    companion object {
        fun of(milestone: Milestone): MilestoneCardStyle =
            if (milestone.isHundredUnit) HUNDRED_UNIT else REGULAR
    }
}

@Composable
fun MilestoneCardDialog(
    milestone: Milestone,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val imageExporter = rememberImageExporter()
    val messageController = LocalMessageController.current
    val fileName = "hilingual_milestone_${milestone.count}"

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false,
            dismissOnClickOutside = false,
        ),
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(HilingualTheme.colors.dim2),
            contentAlignment = Alignment.Center,
        ) {
            MilestoneCardExportLayout(
                milestone = milestone,
                modifier = imageExporter.recordModifier,
            )

            MilestoneCardPopup(
                milestone = milestone,
                onCloseClick = onDismiss,
                onShareClick = { imageExporter.share(fileName) },
                onSaveClick = {
                    imageExporter.saveToGallery(
                        fileName = fileName,
                        onSuccess = {
                            messageController(HilingualMessage.Toast("카드가 이미지로 저장되었어요."))
                        },
                    )
                },
            )
        }
    }
}

@Composable
private fun MilestoneCardPopup(
    milestone: Milestone,
    onCloseClick: () -> Unit,
    onShareClick: () -> Unit,
    onSaveClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(
                color = HilingualTheme.colors.white,
                shape = RoundedCornerShape(12.dp),
            ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            MilestoneCardContent(milestone = milestone)

            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                MilestoneActionButton(
                    iconRes = R.drawable.ic_share_24,
                    label = "공유",
                    onClick = onShareClick,
                )
                MilestoneActionButton(
                    iconRes = R.drawable.ic_download_24,
                    label = "저장",
                    onClick = onSaveClick,
                )
            }
        }

        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_close_circle_22),
            contentDescription = "닫기",
            tint = Color.Unspecified,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 12.dp, end = 12.dp)
                .noRippleClickable(onClick = onCloseClick),
        )
    }
}

@Composable
private fun MilestoneCardExportLayout(
    milestone: Milestone,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    CompositionLocalProvider(LocalDensity provides Density(density = density.density, fontScale = 1f)) {
        Box(
            modifier = modifier
                .requiredSize(EXPORT_IMAGE_SIZE)
                .background(HilingualTheme.colors.gray100),
            contentAlignment = Alignment.Center,
        ) {
            MilestoneCardContent(
                milestone = milestone,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .background(
                        color = HilingualTheme.colors.white,
                        shape = RoundedCornerShape(12.dp),
                    )
                    .padding(horizontal = 24.dp, vertical = 32.dp),
            )
        }
    }
}

@Composable
private fun MilestoneCardContent(
    milestone: Milestone,
    modifier: Modifier = Modifier,
) {
    val style = MilestoneCardStyle.of(milestone)
    val pointColor = style.pointColor

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Milestone Unlocked",
                style = HilingualTheme.typography.headSB20,
                color = HilingualTheme.colors.black,
            )
            Text(
                text = "${milestone.count}",
                style = HilingualTheme.typography.headEB90,
                color = pointColor,
            )
            Text(
                text = "STORIES",
                style = HilingualTheme.typography.headB36,
                color = HilingualTheme.colors.black,
            )
            Image(
                painter = painterResource(style.illustrationRes),
                contentDescription = null,
                modifier = Modifier.size(width = 280.dp, height = 152.dp),
            )
        }

        Text(
            text = style.message(count = milestone.count, pointColor = pointColor),
            style = HilingualTheme.typography.bodyM14,
            color = HilingualTheme.colors.black,
            textAlign = TextAlign.Center,
        )

        Image(
            imageVector = ImageVector.vectorResource(R.drawable.img_logo_small_gray),
            contentDescription = null,
        )
    }
}

private fun MilestoneCardStyle.message(count: Int, pointColor: Color): AnnotatedString = buildAnnotatedString {
    append(messagePrefix)
    withStyle(SpanStyle(color = pointColor)) { append("$count $countUnit") }
    append(messageSuffix)
}

@Composable
private fun MilestoneActionButton(
    @DrawableRes iconRes: Int,
    label: String,
    onClick: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.noRippleClickable(onClick = onClick),
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(color = HilingualTheme.colors.gray100, shape = CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(iconRes),
                contentDescription = null,
                tint = Color.Unspecified,
            )
        }
        Text(
            text = label,
            style = HilingualTheme.typography.captionR12,
            color = HilingualTheme.colors.gray700,
        )
    }
}

@Preview
@Composable
private fun MilestoneCardPopupPreview() {
    HilingualTheme {
        MilestoneCardPopup(
            milestone = requireNotNull(Milestone.fromOrNull(30)),
            onCloseClick = {},
            onShareClick = {},
            onSaveClick = {},
        )
    }
}

@Preview(widthDp = 360, heightDp = 640)
@Composable
private fun MilestoneCardExportLayoutPreview() {
    HilingualTheme {
        MilestoneCardExportLayout(
            milestone = requireNotNull(Milestone.fromOrNull(100)),
        )
    }
}
