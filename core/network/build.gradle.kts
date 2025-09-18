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
    alias(libs.plugins.kotlin.serialization)
}

android {
    setNamespace("core.network")

    defaultConfig {
        consumerProguardFile("consumer-rules.pro")
    }

    val properties = Properties().apply {
        load(rootProject.file("local.properties").inputStream())
    }

    buildTypes {
        debug {
            val devUrl = properties["dev.base.url"] as? String ?: ""
            buildConfigField("String", "BASE_URL", devUrl)
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
    implementation(projects.core.common)
    implementation(projects.core.localstorage)

    // others
    implementation(platform(libs.okhttp.bom))
    implementation(libs.bundles.okhttp)
    implementation(libs.bundles.retrofit)
    implementation(platform(libs.coil.bom))
    implementation(libs.bundles.coil)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.timber)
}
