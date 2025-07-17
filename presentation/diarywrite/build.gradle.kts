import com.hilingual.build_logic.setNamespace

plugins {
    alias(libs.plugins.hilingual.feature)
}

android {
    setNamespace("presentation.diarywrite")
}

dependencies {
    implementation(libs.balloon.compose)
    implementation(projects.data.calendar)

    // ML Kit
    implementation("com.google.mlkit:text-recognition:16.0.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.1")
}
