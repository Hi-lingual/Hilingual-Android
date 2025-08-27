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

plugins {
    alias(libs.plugins.hilingual.feature)
}

android {
    setNamespace("presentation.main")

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    // feature
    implementation(projects.presentation.home)
    implementation(projects.presentation.auth)
    implementation(projects.presentation.diaryfeedback)
    implementation(projects.presentation.diarywrite)
    implementation(projects.presentation.voca)
    implementation(projects.presentation.onboarding)
    implementation(projects.presentation.splash)
    implementation(projects.presentation.mypage)
    implementation(projects.presentation.otp)
    implementation(projects.presentation.feed)
    implementation(projects.presentation.feeddiary)
    implementation(projects.presentation.feedprofile)
}
