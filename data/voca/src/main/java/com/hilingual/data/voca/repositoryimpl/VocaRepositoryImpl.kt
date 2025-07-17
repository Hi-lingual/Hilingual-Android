package com.hilingual.data.voca.repositoryimpl

import com.hilingual.core.common.util.suspendRunCatching
import com.hilingual.data.voca.datasource.VocaDataSource
import com.hilingual.data.voca.model.VocaDetailModel
import com.hilingual.data.voca.model.VocaListResultModel
import com.hilingual.data.voca.model.toModel
import com.hilingual.data.voca.repository.VocaRepository
import javax.inject.Inject

internal class VocaRepositoryImpl @Inject constructor(
    private val vocaDataSource: VocaDataSource
) : VocaRepository {
    override suspend fun getVocaList(sort: Int): Result<VocaListResultModel> =
        suspendRunCatching {
            vocaDataSource.getVocaList(sort = sort).data!!.toModel()
        }

    override suspend fun getVocaDetail(phraseId: Long): Result<VocaDetailModel> =
        suspendRunCatching {
            vocaDataSource.getVocaDetail(phraseId = phraseId).data!!.toModel()
        }
}
