import com.hilingual.build_logic.setNamespace

plugins {
    alias(libs.plugins.hilingual.data)
}

android {
    setNamespace("data.login")
}

dependencies {
    // Google Auth
    implementation(libs.androidx.credentials)
    implementation(libs.googleid)
    implementation(libs.androidx.credentials.play.services.auth)
}