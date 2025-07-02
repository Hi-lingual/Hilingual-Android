import com.hilingual.build_logic.configureHiltAndroid
import com.hilingual.build_logic.configureSerializationAndroid
import com.hilingual.build_logic.libs

plugins {
    id("hilingual.android.library")
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
    implementation(project(":core:network"))
    implementation(project(":core:common"))
    implementation(project(":core:localstorage"))

    // timber
    implementation(libs.findLibrary("timber").get())

    // retrofit
    implementation(libs.findBundle("retrofit").get())
}