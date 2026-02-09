import com.hilingual.buildlogic.setNamespace

plugins {
    alias(libs.plugins.hilingual.android.presentation)
}

android {
    setNamespace("presentation.signup")
}

dependencies {
    implementation(projects.data.user)
    implementation(projects.data.config)
}
