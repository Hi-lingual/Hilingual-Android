/*
 * Copyright 2025 The Hilingual Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
            storeFile = File("${project.rootDir.absolutePath}/keystore/hilingual-debug-key.jks")
            storePassword = "hilingual-debug-key"
        }

        create("release") {
            keyAlias = properties["releaseKeyAlias"] as? String ?: ""
            keyPassword = properties["releaseKeyPassword"] as? String ?: ""
            storeFile = File("${project.rootDir.absolutePath}/keystore/hilingual-release-key.jks")
            storePassword = "hilingual-release-key"
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
            buildConfigField(
                "String",
                "BASE_URL",
                properties["prod.base.url"] as String
            )

            isMinifyEnabled = true
            isShrinkResources = true
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
    implementation(libs.androidx.credentials.play.services.auth)
}

ktlint {
    android = true
    debug = true
    coloredOutput = true
    verbose = true
    outputToConsole = true
}
