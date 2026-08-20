import com.hilingual.buildlogic.setNamespace

plugins {
    alias(libs.plugins.hilingual.android.presentation)
}

android {
    setNamespace("presentation.widget")
}

dependencies {
    implementation(projects.data.diary)
    implementation(projects.data.voca)

    implementation(libs.androidx.glance.appwidget)
    implementation(libs.androidx.glance.material)
    debugImplementation(libs.androidx.glance.preview)
    debugImplementation(libs.androidx.glance.appwidget.preview)
}
