package com.hilingual.data.widget.repositoryimpl

import com.hilingual.core.common.util.suspendRunCatching
import com.hilingual.core.common.util.toIsoDate
import com.hilingual.data.widget.datasource.WidgetRemoteDataSource
import com.hilingual.data.widget.model.WidgetTopicModel
import com.hilingual.data.widget.model.toModel
import com.hilingual.data.widget.repository.WidgetRepository
import java.time.LocalDate
import javax.inject.Inject

internal class WidgetRepositoryImpl @Inject constructor(
    private val widgetRemoteDataSource: WidgetRemoteDataSource,
) : WidgetRepository {
    override suspend fun getTopic(date: LocalDate): Result<WidgetTopicModel> =
        suspendRunCatching {
            widgetRemoteDataSource.getTopic(date = date.toIsoDate()).data!!.toModel()
        }
}
