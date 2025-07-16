package com.hilingual.data.voca.repository

import com.hilingual.data.voca.model.VocaDetailModel
import com.hilingual.data.voca.model.VocaListResultModel

interface VocaRepository {
    suspend fun getVocaList(
        sort: Int
    ): Result<VocaListResultModel>

    suspend fun getVocaDetail(
        phraseId: Long
    ): Result<VocaDetailModel>
}
