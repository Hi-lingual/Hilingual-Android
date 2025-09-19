package com.hilingual.baselineprofile

import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * 대상 패키지에 대한 기본 시작 베이스라인 프로파일을 생성하는 테스트 클래스입니다.
 *
 * 여기에서 시작하여 중요한 사용자 흐름을 프로파일에 추가하여 성능을 개선하는 것을 권장합니다.
 * 자세한 내용은 [베이스라인 프로파일 문서](https://d.android.com/topic/performance/baselineprofiles)를 참조하세요.
 *
 * Android Studio의 "Generate Baseline Profile" 실행 구성을 사용하거나
 * 해당하는 `generateBaselineProfile` gradle 태스크를 사용하여 생성기를 실행할 수 있습니다:
 * ```
 * ./gradlew :app:generateReleaseBaselineProfile
 * ```
 * 실행 구성은 Gradle 태스크를 실행하고 필터링을 적용하여 생성기만 실행합니다.
 *
 * 사용 가능한 instrumentation argument에 대한 자세한 내용은 [문서](https://d.android.com/topic/performance/benchmarking/macrobenchmark-instrumentation-args)를 확인하세요.
 *
 * 생성기를 실행한 후에는 [StartupBenchmarks] 벤치마크를 실행하여 개선 사항을 확인할 수 있습니다.
 *
 * 이 클래스를 사용하여 베이스라인 프로파일을 생성할 때는 API 33+ 또는 루팅된 API 28+만 지원됩니다.
 *
 * 베이스라인 프로파일을 생성하는 데 필요한 최소 androidx.benchmark 버전은 1.2.0입니다.
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class BaselineProfileGenerator {

    @get:Rule
    val rule = BaselineProfileRule()

    @Test
    fun generate() {
        // 실행 중인 빌드 variant의 application id는 instrumentation arguments에서 읽어옵니다.
        rule.collect(
            packageName = InstrumentationRegistry.getArguments().getString("targetAppId")
                ?: throw Exception("targetAppId가 instrumentation runner 인수로 전달되지 않았습니다."),

            // 참고: https://d.android.com/topic/performance/baselineprofiles/dex-layout-optimizations
            includeInStartupProfile = true
        ) {
            // 이 블록은 앱의 핵심 사용자 여정을 정의합니다. 여기서는 앱 시작 최적화에 중점을 둡니다.
            // 하지만 가장 중요한 UI를 탐색하고 스크롤할 수도 있습니다.

            // 앱의 기본 액티비티를 시작합니다.
            pressHome()
            startActivityAndWait()

            // TODO: 앱의 고급 여정을 최적화하기 위한 추가 상호작용을 작성하세요.
            // 예시:
            // 1. 비동기적으로 콘텐츠가 로드될 때까지 기다립니다.
            // 2. 피드 콘텐츠를 스크롤합니다.
            // 3. 상세 화면으로 이동합니다.

            // 앱과 상호작용하는 방법에 대한 자세한 내용은 UiAutomator 문서를 확인하세요.
            // https://d.android.com/training/testing/other-components/ui-automator
        }
    }
}
