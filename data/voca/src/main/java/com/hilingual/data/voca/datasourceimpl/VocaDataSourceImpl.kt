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
package com.hilingual.data.voca.datasourceimpl

import com.hilingual.core.network.BaseResponse
import com.hilingual.data.voca.datasource.VocaDataSource
import com.hilingual.data.voca.dto.response.VocaDetailResponseDto
import com.hilingual.data.voca.dto.response.VocaListResponseDto
import com.hilingual.data.voca.service.VocaService
import javax.inject.Inject

internal class VocaDataSourceImpl @Inject constructor(
    private val vocaService: VocaService
) : VocaDataSource {
    override suspend fun getVocaList(sort: Int): BaseResponse<VocaListResponseDto> =
        vocaService.getVocaList(sort = sort)

    override suspend fun getVocaDetail(phraseId: Long): BaseResponse<VocaDetailResponseDto> =
        vocaService.getVocaDetail(phraseId = phraseId)
}
