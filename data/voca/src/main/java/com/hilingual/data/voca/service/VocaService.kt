package com.hilingual.data.voca.service

import com.hilingual.core.network.BaseResponse
import com.hilingual.data.voca.dto.response.VocaDetailResponseDto
import com.hilingual.data.voca.dto.response.VocaListResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface VocaService {
    @GET("api/v1/voca")
    suspend fun getVocaList(
        @Query("sort") sort: Int
    ): BaseResponse<VocaListResponseDto>

    @GET("api/v1/voca/{phraseId}")
    suspend fun getVocaDetail(
        @Path("phraseId") phraseId: Long
    ): BaseResponse<VocaDetailResponseDto>
}
