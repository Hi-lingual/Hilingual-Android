import com.hilingual.build_logic.setNamespace

plugins {
    alias(libs.plugins.hilingual.library)
    alias(libs.plugins.hilingual.compose)
}

android {
    setNamespace("core.designsystem")
}

dependencies {
    // core
    implementation(projects.core.common)

    // androidx
    implementation(libs.bundles.androidx)

    // others
    implementation(libs.coil.compose)
    implementation(libs.kotlinx.immutable)
    implementation(libs.lottie)
    implementation(libs.pebble)
}
