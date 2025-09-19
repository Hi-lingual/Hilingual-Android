import com.android.build.api.dsl.ApplicationExtension
import com.hilingual.build_logic.configureBuildTypes
import com.hilingual.build_logic.configureHiltAndroid
import com.hilingual.build_logic.configureKotlinAndroid

plugins {
    id("com.android.application")
}

configureKotlinAndroid()
configureHiltAndroid()

extensions.configure<ApplicationExtension> {
    configureBuildTypes(this)

    buildTypes.getByName("benchmark") {
        signingConfig = signingConfigs.getByName("debug")
    }

    sourceSets {
        getByName("benchmark") {
            res.srcDirs("src/release/res")
        }
    }
}