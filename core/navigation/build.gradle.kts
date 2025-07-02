import com.hilingual.build_logic.setNamespace

plugins {
    alias(libs.plugins.hilingual.library)
}

android {
    setNamespace("core.navigation")
}