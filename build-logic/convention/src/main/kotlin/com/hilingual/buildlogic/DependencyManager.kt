package com.hilingual.buildlogic

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.project

object DependencyManager {
    fun addAndroidLibraryDependencies(project: Project) {
        project.dependencies {
            add("coreLibraryDesugaring", project.libs.findLibrary("desugar-jdk-libs").get())

            add("implementation", project.libs.findLibrary("coroutines-core").get())
            add("implementation", project.libs.findLibrary("coroutines-android").get())
        }
    }

    fun addComposeDependencies(project: Project) {
        project.dependencies {
            val bom = project.libs.findLibrary("androidx-compose-bom").get()
            add("implementation", platform(bom))
            add("implementation", project.libs.findBundle("compose").get())
            add("debugImplementation", project.libs.findLibrary("androidx-ui-tooling").get())
            add("debugImplementation", project.libs.findLibrary("androidx-ui-test-manifest").get())
        }
    }

    fun addHiltDependencies(project: Project) {
        project.dependencies {
            add("implementation", project.libs.findLibrary("hilt.android").get())
            add("ksp", project.libs.findLibrary("hilt.compiler").get())
            add("kspAndroidTest", project.libs.findLibrary("hilt.compiler").get())
        }
    }

    fun addSerializationDependencies(project: Project) {
        project.dependencies {
            add("implementation", project.libs.findLibrary("kotlinx.serialization.json").get())
        }
    }

    fun addPresentationDependencies(project: Project) {
        project.dependencies {
            // 모듈
            add("implementation", project.project(":core:common"))
            add("implementation", project.project(":core:designsystem"))
            add("implementation", project.project(":core:ui"))
            add("implementation", project.project(":core:navigation"))

            // AndroidX
            add("implementation", project.libs.findBundle("androidx").get())

            // Navigation
            add("implementation", project.libs.findLibrary("hilt-navigation-compose").get())
            add("androidTestImplementation", project.libs.findLibrary("androidx-ui-test-junit4").get())

            // Timber
            add("implementation", project.libs.findLibrary("timber").get())

            // Immutable
            add("implementation", project.libs.findLibrary("kotlinx-immutable").get())
        }
    }

    fun addDataDependencies(project: Project) {
        project.dependencies {
            // 모듈
            add("implementation", project.project(":core:network"))
            add("implementation", project.project(":core:common"))
            add("implementation", project.project(":core:localstorage"))

            // Timber
            add("implementation", project.libs.findLibrary("timber").get())

            // Retrofit
            add("implementation", project.libs.findBundle("retrofit").get())
        }
    }
}
