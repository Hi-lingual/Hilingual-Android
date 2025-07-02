import com.hilingual.build_logic.setNamespace
import java.util.Properties

plugins {
    alias(libs.plugins.hilingual.library)
    alias(libs.plugins.hilingual.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    setNamespace("core.common")

    val properties = Properties().apply {
        load(rootProject.file("local.properties").inputStream())
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    // okhttp
    implementation(platform(libs.okhttp.bom))
    implementation(libs.bundles.okhttp)

    // serialization
    implementation(libs.bundles.retrofit)

    // Timber
    implementation(libs.timber)
}