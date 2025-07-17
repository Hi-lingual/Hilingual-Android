import java.util.Properties

plugins {
    alias(libs.plugins.hilingual.application)
    alias(libs.plugins.ktlint)
}

val properties = Properties().apply {
    load(project.rootProject.file("local.properties").inputStream())
}

android {
    namespace = "com.hilingual"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.hilingual"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        getByName("debug") {
            keyAlias = properties["debugKeyAlias"] as? String ?: ""
            keyPassword = properties["debugKeyPassword"] as? String ?: ""
            storeFile = File("${project.rootDir.absolutePath}/keystore/hi-lingual-debug-key.jks")
            storePassword = "hi-lingual-debug-key"
        }

        create("release") {
            keyAlias = properties["releaseKeyAlias"] as? String ?: ""
            keyPassword = properties["releaseKeyPassword"] as? String ?: ""
            storeFile = File("${project.rootDir.absolutePath}/keystore/hi-lingual-release-key.jks")
            storePassword = "hi-lingual-release-key"
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            buildConfigField(
                "String",
                "BASE_URL",
                properties["dev.base.url"] as String
            )
        }

        release {
            applicationIdSuffix = ".release"
            buildConfigField(
                "String",
                "BASE_URL",
                properties["prod.base.url"] as String
            )

            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = libs.versions.jvmTarget.get()
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    // presentation
    implementation(projects.presentation.main)

    // data
    implementation(projects.data.auth)
    implementation(projects.data.calendar)
    implementation(projects.data.diary)
    implementation(projects.data.user)
    implementation(projects.data.voca)

    // other dependencies
    implementation(libs.timber)
    implementation(libs.androidx.appcompat)
}

ktlint {
    android = true
    debug = true
    coloredOutput = true
    verbose = true
    outputToConsole = true
}
