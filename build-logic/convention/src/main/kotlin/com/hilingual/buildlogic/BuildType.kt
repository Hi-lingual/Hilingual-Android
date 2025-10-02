package com.hilingual.buildlogic

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import java.util.Properties

fun Project.configureBuildTypes(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    val properties = Properties().apply {
        project.rootProject.file("local.properties").takeIf { it.exists() }?.inputStream()?.use { load(it) }
    }

    commonExtension.apply {
        buildFeatures {
            buildConfig = true
        }

        buildTypes {
            debug {
                buildConfigField(
                    "String",
                    "BASE_URL",
                    properties.getQuotedProperty("dev.base.url")
                )
                buildConfigField(
                    "String",
                    "GOOGLE_WEB_CLIENT_ID",
                    properties.getQuotedProperty("dev.google.client.id")
                )
                buildConfigField(
                    "String",
                    "ADMIN_OTP_CODE",
                    properties.getQuotedProperty("otpcode")
                )
            }
            release {
                buildConfigField(
                    "String",
                    "BASE_URL",
                    properties.getQuotedProperty("prod.base.url")
                )
                buildConfigField(
                    "String",
                    "GOOGLE_WEB_CLIENT_ID",
                    properties.getQuotedProperty("prod.google.client.id")
                )
                buildConfigField(
                    "String",
                    "ADMIN_OTP_CODE",
                    properties.getQuotedProperty("otpcode")
                )
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