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
    alias(libs.plugins.hilingual.android.presentation)
}

android {
    setNamespace("presentation.notification")
}

dependencies {
    implementation(projects.data.notification)
    implementation(projects.data.user)

    // richeditor-compose(Compose Multiplatform)가 org.jetbrains.compose.*를 전이 의존성으로 끌고 와
    // androidx.compose.*와 중복 로드되면, 리마인드 시간 설정 화면의 TimeInput(OutlinedTextField) 렌더링 시
    // AbstractMethodError(CustomStyle.applyStyle)로 크래시가 난다. 이를 막기 위해 제외한다.
    implementation(libs.richeditor.compose) {
        exclude(group = "org.jetbrains.compose.foundation")
        exclude(group = "org.jetbrains.compose.material")
        exclude(group = "org.jetbrains.compose.material3")
        exclude(group = "org.jetbrains.compose.animation")
        exclude(group = "org.jetbrains.compose.runtime")
        exclude(group = "org.jetbrains.compose.ui")
    }
}
