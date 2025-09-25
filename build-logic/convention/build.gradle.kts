import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

group = "com.hilingual.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradle.plugin)
    compileOnly(libs.compose.compiler.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "hilingual.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = "hilingual.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidPresentation") {
            id = "hilingual.android.presentation"
            implementationClass = "AndroidPresentationConventionPlugin"
        }
        register("androidData") {
            id = "hilingual.android.data"
            implementationClass = "AndroidDataConventionPlugin"
        }
        register("androidCompose") {
            id = "hilingual.android.compose"
            implementationClass = "AndroidComposeConventionPlugin"
        }
        register("hilt") {
            id = "hilingual.hilt"
            implementationClass = "HiltConventionPlugin"
        }
        register("serialization") {
            id = "hilingual.serialization"
            implementationClass = "SerializationConventionPlugin"
        }
        register("androidTest") {
            id = "hilingual.android.test"
            implementationClass = "AndroidTestConventionPlugin"
        }
    }
}
