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
import com.hilingual.buildlogic.setNamespace

plugins {
    alias(libs.plugins.hilingual.android.library)
    alias(libs.plugins.hilingual.android.compose)
}

android {
    setNamespace("core.designsystem")
}

dependencies {
    // core
    implementation(projects.core.common)

    // androidx
    implementation(libs.bundles.androidx)

    // others
    implementation(platform(libs.coil.bom))
    implementation(libs.bundles.coil)
    implementation(libs.kotlinx.immutable)
    implementation(libs.pebble)
}
