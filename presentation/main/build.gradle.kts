import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.hilingual.build_logic.setNamespace

plugins {
    alias(libs.plugins.hilingual.feature)
}

val properties = gradleLocalProperties(rootDir, providers)

android {
    setNamespace("presentation.main")

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    // feature
    implementation(projects.presentation.home)
    implementation(projects.presentation.auth)
    implementation(projects.presentation.diaryfeedback)
    implementation(projects.presentation.diarywrite)
    implementation(projects.presentation.voca)
    implementation(projects.presentation.onboarding)
    implementation(projects.presentation.splash)

    // others
    implementation(libs.accompanist.systemuicontroller)
}