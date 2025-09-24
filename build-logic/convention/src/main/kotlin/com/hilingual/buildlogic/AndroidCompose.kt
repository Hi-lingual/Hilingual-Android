package com.hilingual.buildlogic

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension

/**
 * Jetpack Compose에 필요한 세부 설정을 구성합니다.
 * (AGP 8.x 이상에서는 compose 플러그인 적용 시 compose buildFeatures가 자동으로 활성화됩니다.)
 */
fun Project.configureAndroidCompose(commonExtension: CommonExtension<*, *, *, *, *, *>) {
    // 현재는 특별한 세부 설정이 없지만, 향후 추가될 것을 대비해 구조를 유지합니다.
    // 예를 들어, 아래와 같은 컴파일러 관련 설정을 추가할 수 있습니다.
    extensions.getByType<ComposeCompilerGradlePluginExtension>().apply {
        includeSourceInformation.set(true)
    }
}
