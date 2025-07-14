package com.hilingual.data.voca.datasource

import com.hilingual.core.network.BaseResponse
import com.hilingual.data.voca.dto.response.VocaDetailResponseDto
import com.hilingual.data.voca.dto.response.VocaListResponseDto

interface VocaDataSource {
    suspend fun getVocaList(
        sort: Int
    ):  BaseResponse<VocaListResponseDto>

    suspend fun getVocaDetail(
        phraseId: Long
    ): BaseResponse<VocaDetailResponseDto>

}