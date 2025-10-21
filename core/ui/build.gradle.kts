import com.hilingual.buildlogic.setNamespace

plugins {
    alias(libs.plugins.hilingual.android.library)
    alias(libs.plugins.hilingual.android.compose)
}

android {
    setNamespace("core.ui")
}

dependencies {
    // core
    implementation(projects.core.common)
    implementation(projects.core.designsystem)

    // androidx
    implementation(libs.bundles.androidx)

    // others
    implementation(platform(libs.coil.bom))
    implementation(libs.bundles.coil)
    implementation(libs.lottie)
    implementation(libs.kotlinx.immutable)
}
