package com.hilingual.data.diary.repositoryimpl

import com.hilingual.core.common.util.suspendRunCatching
import com.hilingual.data.diary.datasource.DiaryRemoteDataSource
import com.hilingual.data.diary.dto.response.DiaryFeedbackCreateResposeDto
import com.hilingual.data.diary.model.DiaryContentModel
import com.hilingual.data.diary.model.DiaryFeedbackModel
import com.hilingual.data.diary.model.DiaryRecommendExpressionModel
import com.hilingual.data.diary.model.PhraseBookmarkModel
import com.hilingual.data.diary.model.toDto
import com.hilingual.data.diary.model.toModel
import com.hilingual.data.diary.repository.DiaryRepository
import java.io.File
import javax.inject.Inject

internal class DiaryRepositoryImpl @Inject constructor(
    private val diaryRemoteDataSource: DiaryRemoteDataSource
) : DiaryRepository {
    override suspend fun getDiaryContent(diaryId: Long): Result<DiaryContentModel> =
        suspendRunCatching {
            diaryRemoteDataSource.getDiaryContent(diaryId).data!!.toModel()
        }

    override suspend fun getDiaryFeedbacks(diaryId: Long): Result<List<DiaryFeedbackModel>> =
        suspendRunCatching {
            diaryRemoteDataSource.getDiaryFeedbacks(diaryId).data!!.feedbackList.map { it.toModel() }
        }

    override suspend fun getDiaryRecommendExpressions(diaryId: Long): Result<List<DiaryRecommendExpressionModel>> =
        suspendRunCatching {
            diaryRemoteDataSource.getDiaryRecommendExpressions(diaryId).data!!.phraseList.map { it.toModel() }
        }

    override suspend fun patchPhraseBookmark(
        phraseId: Long,
        bookmarkModel: PhraseBookmarkModel
    ): Result<Unit> =
        suspendRunCatching {
            diaryRemoteDataSource.patchPhraseBookmark(
                phraseId = phraseId,
                bookmarkRequestDto = bookmarkModel.toDto()
            )
        }

    override suspend fun postDiaryFeedbackCreate(
        originalText: String,
        date: String,
        imageFile: File?
    ): Result<DiaryFeedbackCreateResposeDto> =
        suspendRunCatching {
            diaryRemoteDataSource.getDiaryFeedbackCreate(
                originalText = originalText,
                date = date,
                imageFile = imageFile
            ).data!!
        }
}
