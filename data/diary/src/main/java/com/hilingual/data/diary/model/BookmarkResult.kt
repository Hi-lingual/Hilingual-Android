package com.hilingual.data.diary.model

// sealed interface BookmarkResult {
//    data object isSuccess : BookmarkResult
//    data class isError(val errorType: BookmarkErrorCode) : BookmarkResult
// }

enum class BookmarkResult(val code: Int) {
    DEFAULT(-1),
    SUCCESS(20000),
    OVERCAPACITY(40301);

    companion object {
        fun getErrorResult(code: Int) = entries.find { it.code == code } ?: DEFAULT
    }
}
