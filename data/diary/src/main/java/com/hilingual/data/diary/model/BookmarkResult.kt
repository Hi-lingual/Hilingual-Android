package com.hilingual.data.diary.model

enum class BookmarkResult(val code: Int) {
    DEFAULT(-1),
    SUCCESS(20000),
    OVERCAPACITY(40301);

    companion object {
        fun getErrorResult(code: Int) = entries.find { it.code == code } ?: DEFAULT
    }
}
