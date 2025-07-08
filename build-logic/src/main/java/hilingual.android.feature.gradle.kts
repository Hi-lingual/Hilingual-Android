import com.hilingual.build_logic.configureHiltAndroid
import com.hilingual.build_logic.configureSerializationAndroid
import com.hilingual.build_logic.libs

plugins {
    id("hilingual.android.library")
    id("hilingual.android.compose")
}

android {
    packaging {
        resources {
            excludes.add("META-INF/**")
        }
    }
}

configureHiltAndroid()
configureSerializationAndroid()

dependencies {
    val libs = project.extensions.libs

    // modules
    implementation(project(":core:common"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:navigation"))

    // androidx bundle
    implementation(libs.findBundle("androidx").get())

    // navigation
    implementation(libs.findLibrary("hilt-navigation-compose").get())
    androidTestImplementation(libs.findLibrary("androidx-ui-test-junit4").get())

    // timber
    implementation(libs.findLibrary("timber").get())

    // immutable
    implementation(libs.findLibrary("kotlinx-immutable").get())

    // other
    implementation(libs.findLibrary("accompanist-systemuicontroller").get())
}