import com.hilingual.build_logic.setNamespace
import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.hilingual.data)
}

val properties = Properties().apply {
    load(project.rootProject.file("local.properties").inputStream())
}

android {
    setNamespace("data.auth")

    defaultConfig {
        buildConfigField(
            "String",
            "GOOGLE_WEB_CLIENT_ID",
            properties.getProperty("google.client.id")
        )
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:network"))

    // Google Auth
    api(libs.androidx.credentials)
    api(libs.googleid)
    implementation(libs.androidx.credentials.play.services.auth)
}