package com.hilingual.data.voca.repositoryimpl

import com.hilingual.core.common.util.suspendRunCatching
import com.hilingual.data.voca.datasource.VocaDataSource
import com.hilingual.data.voca.model.VocaDetail
import com.hilingual.data.voca.model.VocaList
import com.hilingual.data.voca.model.toModel
import com.hilingual.data.voca.repository.VocaRepository
import javax.inject.Inject

class VocaRepositoryImpl @Inject constructor(
    private val vocaDataSource: VocaDataSource
) : VocaRepository {
    override suspend fun getVocaList(sort: Int): Result<Pair<Int, List<VocaList>>> =
        suspendRunCatching {
            vocaDataSource.getVocaList(sort = sort).data!!.run {
                Pair(this.count, this.wordList.map { it.toModel() })
            }
        }

    override suspend fun getVocaDetail(phraseId: Long): Result<VocaDetail> =
        suspendRunCatching {
            vocaDataSource.getVocaDetail(phraseId = phraseId).data!!.toModel()
        }
}
