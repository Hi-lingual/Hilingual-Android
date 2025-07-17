import com.hilingual.build_logic.setNamespace

plugins {
    alias(libs.plugins.hilingual.feature)
}

android {
    setNamespace("presentation.auth")
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:designsystem"))
    implementation(project(":data:auth"))
    implementation(libs.androidx.browser)
}
