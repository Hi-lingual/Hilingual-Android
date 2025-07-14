package com.hilingual.data.voca.datasourceimpl

import com.hilingual.core.network.BaseResponse
import com.hilingual.data.voca.datasource.VocaDataSource
import com.hilingual.data.voca.dto.response.VocaDetailResponseDto
import com.hilingual.data.voca.dto.response.VocaListResponseDto
import com.hilingual.data.voca.service.VocaService
import javax.inject.Inject

class VocaDataSourceImpl @Inject constructor(
    private val vocaService: VocaService
) : VocaDataSource {
    override suspend fun getVocaList(sort: Int): BaseResponse<VocaListResponseDto> =
        vocaService.getVocaList(sort = sort)

    override suspend fun getVocaDetail(phraseId: Long): BaseResponse<VocaDetailResponseDto> =
        vocaService.getVocaDetail(phraseId = phraseId)
}