package com.hilingual.core.common.util

object EmojiFilter {
    private val EMOJI_REGEX = Regex("[\\p{So}\\p{Cn}]+")

    fun String.removeEmoji(): String {
        return this.replace(EMOJI_REGEX, "")
    }
}
