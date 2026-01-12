import com.hilingual.buildlogic.setNamespace

plugins {
    alias(libs.plugins.hilingual.android.data)
}

android {
    setNamespace("data.config")
}

dependencies {
    implementation(projects.core.common)
}
