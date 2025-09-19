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
