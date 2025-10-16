/*
 * Copyright 2025 The Hilingual Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hilingual.baselineprofile

import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * 앱의 시작 성능을 측정하고, 베이스라인 프로파일을 생성하는 테스트 클래스입니다.
 *
 * 기본적으로 앱 실행(Startup)을 포함하며,
 * 필요하다면 중요한 사용자 여정(CUJ: Custom User Journey)을 추가해
 * 스크롤, 화면 전환 등 성능을 함께 최적화할 수 있습니다.
 *
 * 프로파일 생성 방법:
 * - Build Varient 를 Release로 선택.
 * - Android Studio 메뉴의 "Generate Baseline Profile for app" 실행
 * - 또는 Gradle 태스크 실행(명령어 기반)
 *   ```
 *   ./gradlew :app:generateReleaseBaselineProfile
 *   ./gradlew :app:generateBaselineProfile <- 내부적으로 Release 빌드에 대해서 실행합니다.
 *   ```
 *
 * 프로파일 생성 후에는 [StartupBenchmarks] 벤치마크로 성능 향상 여부를 확인해주세요.
 *
 * 참고:
 * - 지원 API: 33+ (또는 루팅된 28+ 이상)
 * - 필요 라이브러리 버전: androidx.benchmark 1.2.0 이상
 * - Instrumentation argument `targetAppId`가 반드시 전달되어야 함
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class BaselineProfileGenerator {

    @get:Rule
    val rule = BaselineProfileRule()

    @Test
    fun generate() {
        rule.collect(
            packageName = InstrumentationRegistry.getArguments().getString("targetAppId")
                ?: throw Exception("instrumentation runner에 targetAppId가 전달되지 않았습니다."),
            includeInStartupProfile = true
        ) {
            // 앱 시작 (기본 액티비티)
            pressHome()
            startActivityAndWait()

            // TODO: 필요 시 주요 사용자 여정 추가
            // - 콘텐츠 로드 대기
            // - 피드 스크롤
            // - 상세 화면 이동 등
        }
    }
}
