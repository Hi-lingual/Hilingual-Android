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
import com.hilingual.build_logic.setNamespace
import java.util.Properties

plugins {
    alias(libs.plugins.hilingual.library)
    alias(libs.plugins.hilingual.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    setNamespace("core.common")

    val properties = Properties().apply {
        load(rootProject.file("local.properties").inputStream())
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    // okhttp
    implementation(platform(libs.okhttp.bom))
    implementation(libs.bundles.okhttp)

    // serialization
    implementation(libs.bundles.retrofit)

    // Timber
    implementation(libs.timber)

    implementation(libs.androidx.browser)
}
