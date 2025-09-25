package com.hilingual.buildlogic

import com.android.build.api.dsl.CommonExtension
import com.hilingual.buildlogic.libs
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/**
 * 모든 안드로이드 모듈에 적용될 기본적인 코틀린 및 안드로이드 설정을 구성합니다.
 */
fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        compileSdk = libs.findVersion("compileSdk").get().requiredVersion.toInt()

        defaultConfig {
            minSdk = libs.findVersion("minSdk").get().requiredVersion.toInt()
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
            isCoreLibraryDesugaringEnabled = true
        }


    }

    configureKotlin()
}

/**
 * 모든 코틀린 모듈(Android 포함)에 적용될 공통 코틀린 옵션을 구성합니다.
 */
private fun Project.configureKotlin() {
    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
            // 모든 코틀린 경고를 에러로 처리합니다. (기본적으로 비활성화)
            // 활성화하려면 ~/.gradle/gradle.properties 파일에 warningsAsErrors=true 를 추가하세요.
            val warningsAsErrors: String? by project
            allWarningsAsErrors.set(warningsAsErrors.toBoolean())
            freeCompilerArgs.addAll(
                listOf(
                    "-opt-in=kotlin.RequiresOptIn",
                    "-Xannotation-default-target=param-property",
                )
            )
        }
    }
}
