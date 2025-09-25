package com.hilingual.buildlogic

import com.android.build.api.variant.LibraryAndroidComponentsExtension
import org.gradle.api.Project

/**
 * `androidTest` 소스셋이 없는 라이브러리 모듈의 경우,
 *  불필요한 Android Instrumented Test 작업을 비활성화하여 빌드 시간을 최적화합니다.
 */
fun LibraryAndroidComponentsExtension.disableUnnecessaryAndroidTests(
    project: Project,
) = beforeVariants { variant ->
    variant.androidTest.enable = variant.androidTest.enable
        && project.projectDir.resolve("src/androidTest").exists()
}
