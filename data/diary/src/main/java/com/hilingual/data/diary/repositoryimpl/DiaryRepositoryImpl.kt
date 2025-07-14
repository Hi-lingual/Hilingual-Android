package com.hilingual.data.diary.repositoryimpl

import com.hilingual.data.diary.datasource.DiaryRemoteDataSource
import com.hilingual.data.diary.repository.DiaryRepository
import javax.inject.Inject

internal class DiaryRepositoryImpl @Inject constructor(
    private val diaryRemoteDataSource: DiaryRemoteDataSource
) : DiaryRepository
