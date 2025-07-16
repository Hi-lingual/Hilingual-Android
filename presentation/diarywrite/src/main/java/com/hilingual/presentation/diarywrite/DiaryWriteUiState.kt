package com.hilingual.presentation.diarywrite

import android.net.Uri
import androidx.compose.runtime.Immutable
import java.time.LocalDate

@Immutable
internal data class DiaryWriteUiState(
//    val selectedDate: LocalDate = LocalDate.now(),
    val selectedDate: LocalDate = LocalDate.of(2025, 6, 8),
    val topicKo: String = "",
    val topicEn: String = "",
    val diaryText: String = "",
    var diaryImageUri: Uri? = null
)
