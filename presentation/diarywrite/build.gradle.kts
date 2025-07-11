import com.hilingual.build_logic.setNamespace

plugins {
    alias(libs.plugins.hilingual.feature)
}

android {
    setNamespace("presentation.diarywrite")
}

dependencies {
    implementation("com.github.skydoves:balloon-compose:1.6.12")
}