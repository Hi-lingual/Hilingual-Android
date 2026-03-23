/*
 * Copyright 2026 The Hilingual Project
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
    alias(libs.plugins.hilingual.hilt)
}

android {
    setNamespace("core.ads")
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.designsystem)
    implementation(libs.gma.ads)

    // Workaround for GMA Next Gen SDK beta03 Cronet namespace bug
    val cronetVersion = "143.7445.0"
    implementation("org.chromium.net:cronet-api:$cronetVersion")
    implementation("org.chromium.net:cronet-shared:$cronetVersion")
    implementation("org.chromium.net:cronet-common:$cronetVersion")
    implementation("org.chromium.net:cronet-fallback:$cronetVersion")
    implementation("org.chromium.net:httpengine-native-provider:$cronetVersion")

    implementation(libs.timber)
}
