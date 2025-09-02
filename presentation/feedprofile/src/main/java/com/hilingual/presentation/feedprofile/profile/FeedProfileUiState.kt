package com.hilingual.presentation.feedprofile.profile

import androidx.compose.runtime.Immutable
import com.hilingual.presentation.feedprofile.profile.model.FeedDiaryUIModel
import com.hilingual.presentation.feedprofile.profile.model.FeedProfileInfoModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class FeedProfileUiState(
    val feedProfileInfo: FeedProfileInfoModel,
    val sharedDiaries: ImmutableList<FeedDiaryUIModel> = persistentListOf(),
    val likedDiaries: ImmutableList<FeedDiaryUIModel> = persistentListOf()
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
            sharedDiaries = persistentListOf(
                FeedDiaryUIModel(
                    diaryId = 1L,
                    authorUserId = 0L,
                    authorNickname = "가짜프로필",
                    authorProfileImageUrl = "",
                    authorStreak = null,
                    sharedDate = 1720000000L,
                    likeCount = 12,
                    isLiked = false,
                    diaryImageUrl = null,
                    originalText = "가짜 사용자의 일기 내용입니다.",
                    isMine = true
                ),
                FeedDiaryUIModel(
                    diaryId = 3L,
                    authorUserId = 0L,
                    authorNickname = "가짜프로필",
                    authorProfileImageUrl = "",
                    authorStreak = null,
                    sharedDate = 1720000000L,
                    likeCount = 12,
                    isLiked = false,
                    diaryImageUrl = null,
                    originalText = "가짜 사용자의 일기 내용입니다.",
                    isMine = true
                )
            ),
            likedDiaries = persistentListOf(
                FeedDiaryUIModel(
                    diaryId = 1L,
                    authorUserId = 0L,
                    authorNickname = "가짜프로필",
                    authorProfileImageUrl = "",
                    authorStreak = null,
                    sharedDate = 1720000000L,
                    likeCount = 12,
                    isLiked = false,
                    diaryImageUrl = null,
                    originalText = "가짜 사용자의 일기 내용입니다.",
                    isMine = true
                ),
                FeedDiaryUIModel(
                    diaryId = 10L,
                    authorUserId = 100L,
                    authorNickname = "좋아요한유저",
                    authorProfileImageUrl = "",
                    authorStreak = 7,
                    sharedDate = 1720005000L,
                    likeCount = 34,
                    isLiked = true,
                    diaryImageUrl = null,
                    originalText = "좋아요한 사용자의 일기 내용입니다.",
                    isMine = false
                ),
                FeedDiaryUIModel(
                    diaryId = 11L,
                    authorUserId = 200L,
                    authorNickname = "다른유저",
                    authorProfileImageUrl = "",
                    authorStreak = 15,
                    sharedDate = 1720008000L,
                    likeCount = 8,
                    isLiked = true,
                    diaryImageUrl = null,
                    originalText = "또 다른 사용자의 일기 내용입니다.",
                    isMine = false
                )
            )
        )
    }
}
