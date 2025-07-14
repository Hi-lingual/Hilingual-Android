package com.hilingual.data.diary.datasourceimpl

import com.hilingual.data.diary.datasource.DiaryRemoteDataSource
import com.hilingual.data.diary.service.DiaryService
import javax.inject.Inject

internal class DiaryRemoteRemoteDataSourceImpl @Inject constructor(
    private val diaryService: DiaryService
) : DiaryRemoteDataSource
