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
    val sharedDiaries: ImmutableList<SharedDiaryItemModel> = persistentListOf(),
    val likedDiaries: ImmutableList<LikeDiaryItemModel> = persistentListOf()
) {
    companion object {
        val Fake = FeedProfileUiState(
            feedProfileInfo = FeedProfileInfoModel(
                profileImageUrl = "",
                nickname = "가짜프로필",
                streak = 3,
                follower = 10,
                following = 5,
                isMine = false,
                isFollowing = false,
                isFollowed = false,
                isBlock = false
            ),
            sharedDiaries = persistentListOf(
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
            likedDiaries = persistentListOf(
                LikeDiaryItemModel(
                    userId = 100L,
                    streak = 7,
                    isMine = false,
                    profileImageUrl = "",
                    nickname = "좋아요한유저",
                    diaryId = 10L,
                    sharedDate = 1720005000L,
                    likeCount = 34,
                    isLiked = true,
                    diaryImageUrl = null,
                    originalText = "좋아요한 사용자의 일기 내용입니다."
                ),
                LikeDiaryItemModel(
                    userId = 200L,
                    streak = 15,
                    isMine = false,
                    profileImageUrl = "",
                    nickname = "다른유저",
                    diaryId = 11L,
                    sharedDate = 1720008000L,
                    likeCount = 8,
                    isLiked = true,
                    diaryImageUrl = null,
                    originalText = "또 다른 사용자의 일기 내용입니다."
                )
            )
        )
    }
}
