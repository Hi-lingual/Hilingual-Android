import com.hilingual.build_logic.setNamespace

plugins {
    alias(libs.plugins.hilingual.feature)
}

android {
    setNamespace("presentation.diarywrite")
}

dependencies {
    // coil
    implementation("io.coil-kt:coil-compose:2.4.0")
}