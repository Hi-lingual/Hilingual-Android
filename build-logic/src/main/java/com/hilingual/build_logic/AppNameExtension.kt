package com.hilingual.build_logic

import org.gradle.api.Project

fun Project.setNamespace(name: String) {
    androidExtension.apply {
        namespace = "com.hilingual.$name"
    }
}