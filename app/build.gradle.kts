
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    id("com.google.android.gms.oss-licenses-plugin") version "0.10.7"
}

android {
    namespace = "br.com.rbrthmn"
    compileSdk = 35

    defaultConfig {
        applicationId = "br.com.rbrthmn"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("release") {
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
        aidl = false
        buildConfig = false
        renderScript = false
        shaders = false
    }

    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            merges += "META-INF/LICENSE.md"
            merges += "META-INF/LICENSE-notice.md"
        }
    }
}

dependencies {
    // Core modules
    implementation(project(":core:ui"))
    implementation(project(":core:navigation"))

    // Features
    implementation(project(":feature:auth"))
    implementation(project(":feature:home"))
    implementation(project(":feature:operations"))
    implementation(project(":feature:settings"))
    implementation(project(":feature:misc"))

    // Koin
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.androidx.workmanager)
    implementation(libs.koin.androidx.navigation)

    // Data layer
    implementation(project(":data:auth"))
    implementation(project(":data:common"))
    implementation(project(":data:home"))
    implementation(project(":data:operations"))
    implementation(project(":data:misc"))

    // JUnit KTX
    implementation(libs.androidx.junit.ktx)

    // Testes
    testImplementation(project(":core:test"))
    androidTestImplementation(project(":core:test"))
}