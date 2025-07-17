import com.hilingual.build_logic.setNamespace

plugins {
    alias(libs.plugins.hilingual.feature)
}

android {
    setNamespace("presentation.diaryfeedback")
}

dependencies {
    implementation(project(":data:diary"))
    implementation(libs.androidx.browser)
}
