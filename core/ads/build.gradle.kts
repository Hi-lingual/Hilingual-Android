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

    // Workaround for GMA Next Gen SDK beta03 Cronet namespace bug
    val cronetVersion = "143.7445.0"
    implementation("org.chromium.net:cronet-api:$cronetVersion")
    implementation("org.chromium.net:cronet-shared:$cronetVersion")
    implementation("org.chromium.net:cronet-common:$cronetVersion")
    implementation("org.chromium.net:cronet-fallback:$cronetVersion")
    implementation("org.chromium.net:httpengine-native-provider:$cronetVersion")

    implementation(libs.timber)
}
