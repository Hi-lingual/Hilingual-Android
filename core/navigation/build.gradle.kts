import com.hilingual.build_logic.setNamespace

plugins {
    alias(libs.plugins.hilingual.library)
    alias(libs.plugins.kotlin.serialization)
}

android {
    setNamespace("core.navigation")
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
}