package com.hilingual.presentation.feedprofile.profile

import androidx.compose.runtime.Immutable
import com.hilingual.presentation.feedprofile.profile.model.FeedProfileInfoModel
import com.hilingual.presentation.feedprofile.profile.model.LikeDiaryItemModel
import com.hilingual.presentation.feedprofile.profile.model.SharedDiaryItemModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class FeedProfileUiState(
    val feedProfileInfo: FeedProfileInfoModel,
    val sharedDiarys: ImmutableList<SharedDiaryItemModel> = persistentListOf(),
    val likedDiarys: ImmutableList<LikeDiaryItemModel> = persistentListOf()
) {
    companion object {
        val Fake = FeedProfileUiState(
            feedProfileInfo = FeedProfileInfoModel(
                profileImageUrl = "",
                nickname = "가짜프로필",
                streak = 3,
                follower = 10,
                following = 5,
                isMine = true,
                isFollowing = false,
                isFollowed = false,
                isBlock = false
            ),
            sharedDiarys = persistentListOf(
                SharedDiaryItemModel(
                    profileImageUrl = "",
                    nickname = "가짜프로필",
                    diaryId = 1L,
                    sharedDate = 1720000000L,
                    likeCount = 12,
                    isLiked = false,
                    diaryImageUrl = null,
                    originalText = "가짜 사용자의 일기 내용입니다."
                ),
                SharedDiaryItemModel(
                    profileImageUrl = "",
                    nickname = "가짜프로필",
                    diaryId = 3L,
                    sharedDate = 1720000000L,
                    likeCount = 12,
                    isLiked = false,
                    diaryImageUrl = null,
                    originalText = "가짜 사용자의 일기 내용입니다."
                )
            ),
            likedDiarys = persistentListOf()
        )
    }
}
