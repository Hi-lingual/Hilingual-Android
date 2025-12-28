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
    alias(libs.plugins.hilingual.android.application)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.baselineprofile)
    alias(libs.plugins.aboutlibraries.android)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
}

val properties = Properties().apply {
    project.rootProject.file("local.properties").takeIf { it.exists() }?.inputStream()?.use {
        load(
            it
        )
    }
}

android {
    namespace = "com.hilingual"

    defaultConfig {
        applicationId = "com.hilingual"
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField(
            "String",
            "AMPLITUDE_API_KEY",
            "\"${properties.getProperty("amplitudeKey")}\""
        )
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
            storePassword = properties["releaseStorePassword"] as? String ?: ""
        }
    }

    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".debug"
        }
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
}

dependencies {
    // core
    implementation(projects.core.common)
    implementation(projects.core.work)

    // presentation
    implementation(projects.presentation.main)

    // data
    implementation(projects.data.auth)
    implementation(projects.data.calendar)
    implementation(projects.data.diary)
    implementation(projects.data.user)
    implementation(projects.data.voca)
    implementation(projects.data.feed)

    // other dependencies
    implementation(libs.timber)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.jakewharton.process.phoenix)

    implementation(platform(libs.coil.bom))
    implementation(libs.bundles.coil)

    implementation(libs.androidx.profileinstaller)
    baselineProfile(projects.baselineprofile)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)
}

ktlint {
    android = true
    debug = true
    coloredOutput = true
    verbose = true
    outputToConsole = true
}
