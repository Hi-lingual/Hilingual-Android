package com.hilingual.core.common.analytics

/**
 * 여러 화면에서 공통으로 사용하는 event명 상수.
 *
 * 최종 이벤트명은 [AmplitudeTracker]에서 `${trigger.value}_${page.pageName}.$event` 형식으로 조합되므로,
 * 같은 [event] 문자열이 화면([Page])마다 반복되는 경우 이곳에 상수로 정의해 한 곳에서 관리한다.
 */
object AnalyticsEvent {
    /** 404(NotFound) 오류 화면/다이얼로그에서 이전 화면으로 돌아가는 버튼 클릭. */
    const val DATA_NOT_FOUND_GO_BACK = "data_not_found_go_back"
}
