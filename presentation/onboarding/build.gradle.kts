import com.hilingual.buildlogic.setNamespace

plugins {
    alias(libs.plugins.hilingual.android.presentation)
}

android {
    setNamespace("presentation.onboarding")
}
