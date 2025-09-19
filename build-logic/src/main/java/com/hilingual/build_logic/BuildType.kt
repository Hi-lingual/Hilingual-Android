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

package com.hilingual.build_logic

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import java.util.Properties

internal fun Project.configureBuildTypes(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    val properties = Properties().apply {
        load(project.rootProject.file("local.properties").inputStream())
    }

    commonExtension.apply {
        buildFeatures {
            buildConfig = true
        }

        buildTypes {
            debug {
                buildConfigField("boolean", "IS_BENCHMARK", "false")
                buildConfigField(
                    "String",
                    "BASE_URL",
                    properties["dev.base.url"] as String
                )
                buildConfigField(
                    "String",
                    "GOOGLE_WEB_CLIENT_ID",
                    properties["dev.google.client.id"] as String
                )
            }
            release {
                buildConfigField("boolean", "IS_BENCHMARK", "false")
                buildConfigField(
                    "String",
                    "BASE_URL",
                    properties["prod.base.url"] as String
                )
                buildConfigField(
                    "String",
                    "GOOGLE_WEB_CLIENT_ID",
                    properties["prod.google.client.id"] as String
                )
            }
            create("benchmark") {
                initWith(getByName("release"))
                matchingFallbacks.add("release")
                buildConfigField("boolean", "IS_BENCHMARK", "true")
            }
        }
    }
}
