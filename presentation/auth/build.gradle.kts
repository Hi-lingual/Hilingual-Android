import com.hilingual.build_logic.setNamespace

plugins {
    alias(libs.plugins.hilingual.feature)
}

android {
    setNamespace("presentation.auth")
}