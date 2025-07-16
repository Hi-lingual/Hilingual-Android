import com.hilingual.build_logic.setNamespace

plugins {
    alias(libs.plugins.hilingual.feature)
}

android {
    setNamespace("presentation.diarywrite")
}

dependencies {
    implementation(libs.balloon.compose)
    implementation(projects.data.diary)
}
