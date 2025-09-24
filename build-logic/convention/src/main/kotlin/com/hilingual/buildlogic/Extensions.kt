package com.hilingual.buildlogic

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

val Project.androidExtension: CommonExtension<*, *, *, *, *, *>
    get() = runCatching { extensions.getByType<LibraryExtension>() }
        .recoverCatching { extensions.getByType<ApplicationExtension>() }
        .onFailure { println("Could not find Library or Application extension from this project") }
        .getOrThrow()
