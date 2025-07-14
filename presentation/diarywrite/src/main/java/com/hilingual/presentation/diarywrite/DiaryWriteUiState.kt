package com.hilingual.presentation.diarywrite

import android.net.Uri
import androidx.compose.runtime.Immutable
import java.time.LocalDate

@Immutable
internal data class DiaryWriteUiState(
    val selectedDate: LocalDate = LocalDate.now(),
    val topicKo: String = "",
    val topicEn: String = "",
    val diaryText: String = "",
    val diaryImageUri: Uri? = null
)