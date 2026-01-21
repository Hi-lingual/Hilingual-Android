package com.hilingual.buildlogic

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension

/**
 * Jetpack Compose에 필요한 세부 설정을 구성합니다.
 * (AGP 8.x 이상에서는 compose 플러그인 적용 시 compose buildFeatures가 자동으로 활성화됩니다.)
 */
fun Project.configureAndroidCompose(commonExtension: CommonExtension) {
    extensions.getByType<ComposeCompilerGradlePluginExtension>().apply {
        includeSourceInformation.set(true)
    }
}
