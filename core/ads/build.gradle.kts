import com.hilingual.buildlogic.setNamespace

plugins {
    alias(libs.plugins.hilingual.android.library)
    alias(libs.plugins.hilingual.android.compose)
    alias(libs.plugins.hilingual.hilt)
}

android {
    setNamespace("core.ads")
}

dependencies {
    implementation(projects.core.common)
    implementation(libs.gma.ads)
    implementation(libs.timber)
}
