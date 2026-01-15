import com.hilingual.buildlogic.setNamespace

plugins {
    alias(libs.plugins.hilingual.android.data)
}

android {
    setNamespace("data.config")
}

dependencies {
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.remote.config)
    implementation(libs.firebase.analytics)
    implementation(libs.kotlinx.coroutines.play.services)
}
