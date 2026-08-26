import com.hilingual.buildlogic.setNamespace

plugins {
    alias(libs.plugins.hilingual.android.presentation)
}

android {
    setNamespace("presentation.widget")
}

dependencies {
    implementation(projects.data.widget)

    implementation(libs.androidx.glance.appwidget)
    implementation(libs.androidx.glance.material)
    implementation(libs.androidx.glance.preview)
    debugImplementation(libs.androidx.glance.appwidget.preview)
}
