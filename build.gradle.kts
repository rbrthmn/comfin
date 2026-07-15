
buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    id("com.google.android.gms.oss-licenses-plugin") version "0.10.7" apply false
    alias(libs.plugins.google.services) apply false
}
// Root build.gradle.kts
