package com.hilingual.data.diary.model

sealed interface DiaryFeedbackCreateResult {
    data class Success(val data: DiaryFeedbackCreateModel) : DiaryFeedbackCreateResult

    data class Failure(val status: DiaryFeedbackCreateStatus) : DiaryFeedbackCreateResult
}

enum class DiaryFeedbackCreateStatus(val code: Int) {
    UNKNOWN(-1),
    SUCCESS(20000),

    // TODO: 서버 명세 확정 후 임시 오류 코드(42201, 42202)를 실제 코드로 교체한다.
    TEXT_TOO_LONG(42201),
    SPECIAL_CHARACTER(42202),
    ;

    companion object {
        fun getOrError(code: Int): DiaryFeedbackCreateStatus = entries.find { it.code == code } ?: UNKNOWN
    }
}
