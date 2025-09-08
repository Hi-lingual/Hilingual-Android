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
    alias(libs.plugins.hilingual.data)
}

val properties = Properties().apply {
    load(project.rootProject.file("local.properties").inputStream())
}

android {
    setNamespace("data.auth")

    buildTypes {
        getByName("debug") {
            buildConfigField(
                "String",
                "GOOGLE_WEB_CLIENT_ID",
                properties["dev.google.client.id"] as String
            )
        }
        getByName("release") {
            buildConfigField(
                "String",
                "GOOGLE_WEB_CLIENT_ID",
                properties["prod.google.client.id"] as String
            )
        }
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    // Google Auth
    api(libs.androidx.credentials)
    api(libs.googleid)
    implementation(libs.androidx.credentials.play.services.auth)
}
