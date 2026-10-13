package com.hilingual.data.diary.model

@JvmInline
value class Milestone private constructor(val count: Int) {
    val isHundredUnit: Boolean
        get() = count % HUNDRED_UNIT == 0

    companion object {
        private val EARLY_COUNTS = setOf(7, 30, 50)
        private const val REGULAR_START = 100
        private const val REGULAR_INTERVAL = 50
        private const val HUNDRED_UNIT = 100

        fun from(totalDiaries: Int): Milestone? {
            val isMilestone = totalDiaries in EARLY_COUNTS ||
                (totalDiaries >= REGULAR_START && totalDiaries % REGULAR_INTERVAL == 0)
            return if (isMilestone) Milestone(totalDiaries) else null
        }
    }
}
