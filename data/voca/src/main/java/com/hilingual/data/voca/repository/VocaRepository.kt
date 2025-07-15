package com.hilingual.data.voca.repository

import com.hilingual.data.voca.model.VocaDetail
import com.hilingual.data.voca.model.VocaList

interface VocaRepository {
    suspend fun getVocaList(
        sort: Int
    ): Result<Pair<Int, List<VocaList>>>

    suspend fun getVocaDetail(
        phraseId: Long
    ): Result<VocaDetail>
}
