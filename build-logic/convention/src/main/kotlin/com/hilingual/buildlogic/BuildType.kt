package com.hilingual.buildlogic

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project
import java.util.Properties

fun Project.configureBuildTypes(
    commonExtension: CommonExtension,
) {
    val properties = Properties().apply {
        project.rootProject.file("local.properties").takeIf { it.exists() }?.inputStream()?.use { load(it) }
    }

    fun configureBuildTypeFields(
        buildConfigField: (String, String, String) -> Unit,
        isDebug: Boolean
    ) {
        val prefix = if (isDebug) "dev" else "prod"

        buildConfigField(
            "String",
            "BASE_URL",
            properties.getQuotedProperty("$prefix.base.url")
        )
        buildConfigField(
            "String",
            "GOOGLE_WEB_CLIENT_ID",
            properties.getQuotedProperty("$prefix.google.client.id")
        )
    }

    commonExtension.apply {
        when (this) {
            is ApplicationExtension -> {
                buildFeatures { buildConfig = true }
                buildTypes {
                    getByName("debug") { configureBuildTypeFields(::buildConfigField, true) }
                    getByName("release") { configureBuildTypeFields(::buildConfigField, false) }
                }
            }

            is LibraryExtension -> {
                buildFeatures { buildConfig = true }
                buildTypes {
                    getByName("debug") { configureBuildTypeFields(::buildConfigField, true) }
                    getByName("release") { configureBuildTypeFields(::buildConfigField, false) }
                }
            }
        }
    }
}

private fun Properties.getQuotedProperty(key: String): String {
    val value = getProperty(key) ?: ""
    if (value.startsWith("\"") && value.endsWith("\"")) {
        return value
    }
    return "\"$value\""
}
