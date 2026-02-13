package com.hilingual.core.common.extension

private val EMOJI_REGEX = Regex("[\\p{So}\\p{Cn}]+")

fun String.removeEmoji(): String = this.replace(EMOJI_REGEX, "")
