package com.hilingual.baselineprofile

import androidx.benchmark.macro.BaselineProfileMode
import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * 앱 시작 속도를 측정하는 벤치마크 테스트입니다.
 *
 * - [CompilationMode.None] : Baseline Profile 없이 실행
 * - [CompilationMode.Partial] : Baseline Profile 적용 후 실행
 *
 * 두 모드를 비교하여 **Baseline Profile 최적화 효과**를 확인할 수 있습니다.
 *
 * 실행 방법:
 * - Android Studio에서 instrumentation test 실행
 * - Gradle 태스크 실행 (예: benchmarkRelease):
 *   ```
 *   ./gradlew :baselineprofile:pixel8proApi35BenchmarkReleaseAndroidTest
 *   ```
 *
 * 참고 문서:
 * - [Macrobenchmark 가이드](https://d.android.com/macrobenchmark#create-macrobenchmark)
 * - [Instrumentation arguments](https://d.android.com/topic/performance/benchmarking/macrobenchmark-instrumentation-args)
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
        rule.measureRepeated(
            packageName = InstrumentationRegistry.getArguments().getString("targetAppId")
                ?: throw Exception("instrumentation runner에 targetAppId가 전달되지 않았습니다."),
            metrics = listOf(StartupTimingMetric()),
            compilationMode = compilationMode,
            startupMode = StartupMode.COLD,
            iterations = 10,
            setupBlock = {
                pressHome()
            },
            measureBlock = {
                startActivityAndWait()
                // "Google로 계속하기" 버튼이 표시될 때까지 대기
                // → 앱이 완전히 그려졌다고 판단하는 기준
                device.wait(Until.hasObject(By.text("Google로 계속하기")), 10_000)
            }
        )
    }
}
