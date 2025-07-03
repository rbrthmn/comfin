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

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    plugins {
        // Exemplo para o plugin Kotlin Android (o que você provavelmente precisa)
//        id("org.jetbrains.kotlin.android") version "2.2.0" // Substitua pela versão desejada do Kotlin

        // Exemplo para o plugin KSP
//        id("com.google.devtools.ksp") version "1.9.24-1.0.20" // Substitua pela versão desejada

        // Exemplo para o plugin Compose Compiler (se você não o gerencia via composeOptions)
        // Embora, para o Compose Compiler, a versão seja geralmente gerenciada por
        // `composeOptions.kotlinCompilerExtensionVersion` nos arquivos build.gradle dos módulos,
        // ou mais recentemente, pelo próprio plugin Kotlin.

        // Adicione outros plugins que você quer gerenciar centralmente aqui...
        // id("com.android.library") version "8.2.0"
        // id("org.jetbrains.kotlin.kapt") version "1.9.22"
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "ComFin"

include(":app")
include(":core:test")
include(":core:ui")
include(":core:navigation")
include(":feature:home")
