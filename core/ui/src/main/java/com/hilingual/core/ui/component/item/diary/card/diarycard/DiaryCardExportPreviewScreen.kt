package com.hilingual.core.ui.component.item.diary.card.diarycard

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.provider.MediaStore
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.common.model.HilingualMessage
import com.hilingual.core.common.trigger.LocalMessageController
import com.hilingual.core.designsystem.R
import com.hilingual.core.designsystem.component.bottomsheet.HilingualMenuBottomSheet
import com.hilingual.core.designsystem.component.bottomsheet.HilingualMenuBottomSheetItem
import com.hilingual.core.designsystem.component.image.ErrorImageSize
import com.hilingual.core.designsystem.component.image.NetworkImage
import com.hilingual.core.designsystem.component.toggle.HilingualBasicToggleSwitch
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.core.designsystem.theme.PretendardMedium
import com.hilingual.core.ui.component.dropdown.Topics
import com.hilingual.core.ui.model.diary.DiaryContent
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
internal fun DiaryCardExportPreviewScreen(
    writtenDate: String,
    topics: Topics,
    diaryContent: DiaryContent,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    val messageController = LocalMessageController.current
    val scope = rememberCoroutineScope()
    val cardLayer = rememberGraphicsLayer()
    val shareAction = remember { "${context.packageName}.DIARY_CARD_SHARED.${UUID.randomUUID()}" }
    val currentOnDismiss by rememberUpdatedState(onDismiss)
    var includeTopic by remember { mutableStateOf(true) }
    var isExportMenuVisible by remember { mutableStateOf(false) }

    DisposableEffect(context, shareAction) {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                currentOnDismiss()
            }
        }
        ContextCompat.registerReceiver(
            context,
            receiver,
            IntentFilter(shareAction),
            ContextCompat.RECEIVER_NOT_EXPORTED,
        )
        onDispose { context.unregisterReceiver(receiver) }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        DiaryCardExportPreviewContent(
            writtenDate = writtenDate,
            topics = topics,
            diaryContent = diaryContent,
            includeTopic = includeTopic,
            onIncludeTopicChange = { includeTopic = it },
            onDismiss = onDismiss,
            onExportClick = { isExportMenuVisible = true },
            cardModifier = Modifier.drawWithContent {
                cardLayer.record { this@drawWithContent.drawContent() }
                drawLayer(cardLayer)
            },
        )
    }

    HilingualMenuBottomSheet(
        isVisible = isExportMenuVisible,
        onDismiss = { isExportMenuVisible = false },
    ) {
        HilingualMenuBottomSheetItem(
            text = "이미지 공유하기",
            iconResId = R.drawable.ic_upload_24,
            onClick = {
                isExportMenuVisible = false
                scope.launch {
                    val bitmap = cardLayer.toImageBitmap().asAndroidBitmap()
                    val uri = withContext(Dispatchers.IO) {
                        val file = File(context.cacheDir, "diary-card-${UUID.randomUUID()}.png")
                        FileOutputStream(file).use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
                        FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
                    }
                    val chosenTarget = PendingIntent.getBroadcast(
                        context,
                        0,
                        Intent(shareAction).setPackage(context.packageName),
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
                    )
                    context.startActivity(
                        Intent.createChooser(
                            Intent(Intent.ACTION_SEND).apply {
                                type = "image/png"
                                putExtra(Intent.EXTRA_STREAM, uri)
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            },
                            null,
                            chosenTarget.intentSender,
                        ),
                    )
                }
            },
        )
        HilingualMenuBottomSheetItem(
            text = "이미지 저장하기",
            iconResId = R.drawable.ic_download_24,
            onClick = {
                isExportMenuVisible = false
                scope.launch {
                    val bitmap = cardLayer.toImageBitmap().asAndroidBitmap()
                    val saved = withContext(Dispatchers.IO) {
                        val values = ContentValues().apply {
                            put(MediaStore.Images.Media.DISPLAY_NAME, "diary-card-${UUID.randomUUID()}.png")
                            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
                            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Hilingual")
                        }
                        context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                            ?.let { uri ->
                                context.contentResolver.openOutputStream(uri)?.use {
                                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
                                } ?: false
                            } ?: false
                    }
                    if (saved) {
                        messageController(HilingualMessage.Toast("일기가 이미지로 저장되었어요."))
                        onDismiss()
                    }
                }
            },
        )
    }
}

@Composable
private fun DiaryCardExportPreviewContent(
    writtenDate: String,
    topics: Topics,
    diaryContent: DiaryContent,
    includeTopic: Boolean,
    onIncludeTopicChange: (Boolean) -> Unit,
    onDismiss: () -> Unit,
    onExportClick: () -> Unit,
    cardModifier: Modifier = Modifier,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(HilingualTheme.colors.gray100),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(HilingualTheme.colors.white)
                .height(52.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .noRippleClickable(onClick = onDismiss),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_close_24),
                    contentDescription = "닫기",
                    modifier = Modifier.size(24.dp),
                )
            }
            Text(
                text = "일기 카드 미리보기",
                style = HilingualTheme.typography.headSB18,
                color = HilingualTheme.colors.hilingualBlack,
            )
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_export_44),
                contentDescription = "내보내기",
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(44.dp)
                    .noRippleClickable { onExportClick() },
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(HilingualTheme.colors.white)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "주제 포함하기",
                style = HilingualTheme.typography.bodyR16,
                color = HilingualTheme.colors.black,
            )
            HilingualBasicToggleSwitch(
                isChecked = includeTopic,
                onCheckedChange = onIncludeTopicChange,
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp),
        ) {
            DiaryExportCard(
                writtenDate = writtenDate,
                topics = topics,
                diaryContent = diaryContent,
                includeTopic = includeTopic,
                modifier = cardModifier,
            )
        }
    }
}

@Composable
private fun DiaryExportCard(
    writtenDate: String,
    topics: Topics,
    diaryContent: DiaryContent,
    includeTopic: Boolean,
    modifier: Modifier = Modifier,
) {
    val colors = HilingualTheme.colors
    Box(
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(colors.white)
                .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            if (includeTopic && topics.enTopic.isNotBlank()) {
                Row(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_quote_16),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                    )
                    Text(
                        text = topics.enTopic,
                        style = HilingualTheme.typography.bodyM15,
                        color = colors.gray700,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Image(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_quote_16),
                        contentDescription = null,
                        modifier = Modifier
                            .size(16.dp)
                            .rotate(180f),
                    )
                }
                Spacer(
                    Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(colors.gray100),
                )
            }
            Text(
                text = writtenDate,
                style = HilingualTheme.typography.bodyM12,
                color = colors.gray500,
            )
            diaryContent.imageUrl?.let { imageUrl ->
                NetworkImage(
                    imageUrl = imageUrl,
                    shape = RoundedCornerShape(8.dp),
                    errorImageSize = ErrorImageSize.LARGE,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(178.dp),
                )
            }
            val correctedText = diaryContent.correctedText
            val annotatedText = buildAnnotatedString {
                append(correctedText)
                diaryContent.diffRanges.forEach { (start, end) ->
                    if (start >= 0 && start < end && end <= correctedText.length) {
                        addStyle(
                            SpanStyle(color = colors.hilingualOrange, fontFamily = PretendardMedium),
                            start,
                            end,
                        )
                    }
                }
            }
            Text(
                text = annotatedText,
                style = HilingualTheme.typography.bodyR15,
                color = colors.black,
                maxLines = if (diaryContent.imageUrl == null) 19 else 11,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.heightIn(max = 340.dp),
            )
            Image(
                painter = painterResource(R.drawable.img_logo),
                contentDescription = null,
                colorFilter = ColorFilter.tint(colors.gray200),
                modifier = Modifier
                    .size(width = 80.dp, height = 20.dp)
                    .align(Alignment.CenterHorizontally),
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            repeat(2) {
                Image(
                    painter = painterResource(R.drawable.img_diary_card_binding),
                    contentDescription = null,
                    modifier = Modifier.size(width = 48.dp, height = 22.dp),
                )
            }
        }
    }
}

@Preview
@Composable
private fun DiaryCardExportPreview() {
    HilingualTheme {
        DiaryCardExportPreviewContent(
            writtenDate = "2026.09.28 MON",
            topics = Topics(enTopic = "What surprised you today?"),
            diaryContent = DiaryContent(
                correctedText = "Today I went to the cafe Conhas in Yeonnam to meet my teammates.\n " +
                    "I was planning to arrive around 1:30 p.m., but I got there at 2:20 " +
                    "because I overslept, as always.\n " +
                    "I wore rain boots and brought my favorite umbrella " +
                    "because the weather forecast said it would rain all day, " +
                    "but it wasn’t really raining much outside.\n " +
                    "I got kind of disappointed. But yes, no rain is better than rain, I guess.\n" +
                    "After arriving, I had a jambon arugula sandwich with a vanilla latte.\n " +
                    "Honestly, I should be more careful when I'm drinking milk " +
                    "because I get stomachaches easily, " +
                    "but I always order lattes.\nMy life feels like a disaster, a mess that I call myself.\n " +
                    "But they tasted really good, so I felt more motivated to work.\n " +
                    "I really liked this café because it's spacious, chill, " +
                    "and has a great atmosphere for focusing.\n " +
                    "I’ll definitely come back again soon!",
                diffRanges = persistentListOf(
                    Pair(84, 164),
                    Pair(278, 316),
                    Pair(508, 583),
                    Pair(740, 802),
                ),
            ),
            includeTopic = true,
            onIncludeTopicChange = {},
            onDismiss = {},
            onExportClick = {},
        )
    }
}
