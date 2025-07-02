package com.hilingual.build_logic

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureSerializationAndroid() {
    val libs = extensions.libs
    with(pluginManager) {
        apply(libs.findPlugin("kotlin.serialization").get().get().pluginId)
    }

    dependencies {
        add("implementation", libs.findLibrary("kotlinx.serialization.json").get())
    }
}