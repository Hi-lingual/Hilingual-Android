package com.hilingual.presentation.feed.model

import kotlinx.collections.immutable.toImmutableList

internal enum class FeedTabType(val title: String) {
    RECOMMEND("추천"),
    FOLLOWING("팔로잉");

    companion object {
        val tabTitles = FeedTabType.entries.map { it.title }.toImmutableList()

        fun getTabType(index: Int) = FeedTabType.entries[index]
    }
}