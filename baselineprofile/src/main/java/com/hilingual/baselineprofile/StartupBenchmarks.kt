package com.hilingual.baselineprofile

import androidx.benchmark.macro.BaselineProfileMode
import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * 앱 시작 속도를 벤치마크하는 테스트 클래스입니다.
 * 이 벤치마크를 실행하여 베이스라인 프로파일의 효율성을 확인할 수 있습니다.
 * 베이스라인 프로파일 최적화가 없는 앱을 나타내는 [CompilationMode.None]과
 * 베이스라인 프로파일을 사용하는 [CompilationMode.Partial]을 비교합니다.
 *
 * 이 벤치마크를 실행하면 시작 시간 측정 및 시스템 트레이스 캡처를 통해
 * 베이스라인 프로파일의 효율성을 확인할 수 있습니다. Android Studio에서
 * instrumentation test로 직접 실행하거나, 다음 Gradle 태스크를 사용하여
 * 특정 variant(예: benchmarkRelease)의 모든 벤치마크를 실행할 수 있습니다:
 * ```
 * ./gradlew :baselineprofile:pixel8proApi35BenchmarkReleaseAndroidTest
 * ```
 *
 * 자세한 내용은 [Macrobenchmark 문서](https://d.android.com/macrobenchmark#create-macrobenchmark) 및
 * [instrumentation arguments 문서](https://d.android.com/topic/performance/benchmarking/macrobenchmark-instrumentation-args)를 참조하세요.
 *
 * @see CompilationMode.None
 * @see CompilationMode.Partial
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class StartupBenchmarks {

    @get:Rule
    val rule = MacrobenchmarkRule()

    @Test
    fun startupCompilationNone() =
        benchmark(CompilationMode.None())

    @Test
    fun startupCompilationBaselineProfiles() =
        benchmark(CompilationMode.Partial(BaselineProfileMode.Require))

    private fun benchmark(compilationMode: CompilationMode) {
        // 실행 중인 빌드 variant의 application id는 instrumentation arguments에서 읽어옵니다.
        rule.measureRepeated(
            packageName = InstrumentationRegistry.getArguments().getString("targetAppId")
                ?: throw Exception("targetAppId가 instrumentation runner 인수로 전달되지 않았습니다."),
            metrics = listOf(StartupTimingMetric()),
            compilationMode = compilationMode,
            startupMode = StartupMode.COLD,
            iterations = 10,
            setupBlock = {
                pressHome()
            },
            measureBlock = {
                startActivityAndWait()

                // TODO: 앱이 완전히 그려졌을 때를 기다리기 위한 상호작용을 추가하세요.
                // 앱은 Activity.reportFullyDrawn()이 호출될 때 완전히 그려진 것으로 간주됩니다.
                // Jetpack Compose의 경우, AndroidX Activity 라이브러리의 ReportDrawn, ReportDrawnWhen, ReportDrawnAfter를 사용할 수 있습니다.

                // 앱과 상호작용하는 방법에 대한 자세한 내용은 UiAutomator 문서를 확인하세요.
                // https://d.android.com/training/testing/other-components/ui-automator
            }
        )
    }
}