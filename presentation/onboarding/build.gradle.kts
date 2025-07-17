import com.hilingual.build_logic.setNamespace

plugins {
    alias(libs.plugins.hilingual.feature)
}

android {
    setNamespace("presentation.onboarding")
}

dependencies {
    implementation(projects.data.user)
    implementation(projects.core.localstorage)
}
