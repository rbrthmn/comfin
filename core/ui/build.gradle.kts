/*
 *
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Modifications made by Roberto Kenzo Hamano, 2024
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "br.com.rbrthmn.ui"
    compileSdk = 35

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    // Compose BOM - exposer para todos os módulos dependentes
    api(platform(libs.androidx.compose.bom))

    // Core Compose dependencies - expostas para módulos que dependem de UI
    api(libs.androidx.compose.ui)
    api(libs.androidx.ui.graphics)
    api(libs.androidx.compose.ui.tooling.preview)
    api(libs.androidx.compose.material3)
    api(libs.androidx.runtime.android)

    // Core Android - expostas para acesso aos componentes
    api(libs.androidx.core.ktx)
    api(libs.androidx.appcompat)
    api(libs.material)

    // Koin - expostar para DI em outros módulos
    api(platform(libs.koin.bom))
    api(libs.koin.androidx.compose)
    api(libs.koin.core)
    api(libs.koin.android)

    // Lifecycle para ViewModels
    api(libs.androidx.lifecycle.runtime.ktx)
    api(libs.androidx.lifecycle.viewmodel.compose)
    api(libs.androidx.lifecycle.runtime.compose)

    // Activity Compose
    api(libs.androidx.activity.compose)

    // Material 3 Window Size
    api(libs.androidx.compose.material3.window.size)

    // Debug tools
    debugImplementation(libs.androidx.compose.ui.tooling)
}