/*
 * Copyright 2025 The Hilingual Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
