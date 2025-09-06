package com.hilingual.data.feed.model

import com.hilingual.data.feed.dto.response.RecommendFeedResponseDto
import com.hilingual.data.feed.dto.response.FeedResultDto
import com.hilingual.data.feed.dto.response.FollowingFeedResponseDto

data class FeedListModel(
    val feedList: List<FeedModel>,
    val hasFollowing: Boolean = false
)

data class FeedModel(
    val profile: FeedProfileModel,
    val diary: FeedDiaryPreviewModel
)

data class FeedProfileModel(
    val userId: Long,
    val isMine: Boolean,
    val profileImg: String,
    val nickname: String,
    val streak: Int
)

data class FeedDiaryPreviewModel(
    val diaryId: Long,
    val sharedDate: Long,
    val likeCount: Int,
    val isLiked: Boolean,
    val diaryImg: String?,
    val originalText: String
)

private fun FeedResultDto.toModel(): FeedModel = FeedModel(
    profile = with(this.profile) {
        FeedProfileModel(
            userId = this.userId,
            isMine = this.isMine,
            profileImg = this.profileImg ?: "",
            nickname = this.nickname,
            streak = this.streak
        )
    },
    diary = with(this.diary) {
        FeedDiaryPreviewModel(
            diaryId = this.diaryId,
            sharedDate = this.sharedDate,
            likeCount = this.likeCount,
            isLiked = this.isLiked,
            diaryImg = this.diaryImg,
            originalText = this.originalText
        )
    }
)

internal fun RecommendFeedResponseDto.toModel(): FeedListModel = FeedListModel(
    feedList = this.diaryList.map { it.toModel() }
)

internal fun FollowingFeedResponseDto.toModel(): FeedListModel = FeedListModel(
    feedList = this.diaryList.map { it.toModel() },
    hasFollowing = this.haveFollowing
)