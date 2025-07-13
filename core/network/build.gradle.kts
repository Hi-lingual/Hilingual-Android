import com.hilingual.build_logic.setNamespace
import java.util.Properties

plugins {
    alias(libs.plugins.hilingual.library)
    alias(libs.plugins.kotlin.serialization)
}

android {
    setNamespace("core.network")

    val properties = Properties().apply {
        load(rootProject.file("local.properties").inputStream())
    }

    buildTypes {
        debug {
            val devUrl = properties["dev.base.url"] as? String ?: ""
            buildConfigField("String", "BASE_URL", devUrl)

            val testToken = properties["test.token"] as? String ?: ""
            buildConfigField("String", "TEST_TOKEN", testToken)
        }

        release {
            val prodUrl = properties["prod.base.url"] as? String ?: ""
            buildConfigField("String", "BASE_URL", prodUrl)
        }
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    // core
    implementation(projects.core.localstorage)

    // others
    implementation(platform(libs.okhttp.bom))
    implementation(libs.bundles.okhttp)

    implementation(libs.bundles.retrofit)

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.timber)

    implementation(libs.jakewharton.process.phoenix)
}
