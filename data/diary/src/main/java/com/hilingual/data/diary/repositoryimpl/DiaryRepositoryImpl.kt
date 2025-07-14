package com.hilingual.data.diary.repositoryimpl

import com.hilingual.core.common.util.suspendRunCatching
import com.hilingual.data.diary.datasource.DiaryRemoteDataSource
import com.hilingual.data.diary.model.DiaryContentModel
import com.hilingual.data.diary.model.toModel
import com.hilingual.data.diary.repository.DiaryRepository
import javax.inject.Inject

internal class DiaryRepositoryImpl @Inject constructor(
    private val diaryRemoteDataSource: DiaryRemoteDataSource
) : DiaryRepository {
    override suspend fun getDiaryContent(diaryId: Long): Result<DiaryContentModel> =
        suspendRunCatching {
            diaryRemoteDataSource.getDiaryContent(diaryId).data!!.toModel()
        }
}
