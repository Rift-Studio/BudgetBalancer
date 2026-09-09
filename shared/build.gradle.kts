import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    kotlin("plugin.serialization") version "2.4.10" // Matches Kotlin version
//    kotlin("jvm") version "2.4.10" // Or your current Kotlin version
}

kotlin {
//    listOf(
//        iosArm64(),
//        iosSimulatorArm64()
//    ).forEach { iosTarget ->
//        iosTarget.binaries.framework {
//            baseName = "Shared"
//            isStatic = true
//        }
//    }
    
    jvm()
    
    android {
       namespace = "riftappstudios.finance.budgetbalancer.shared"
       compileSdk = libs.versions.android.compileSdk.get().toInt()
       minSdk = libs.versions.android.minSdk.get().toInt()

       compilerOptions {
           jvmTarget = JvmTarget.JVM_11
       }
       androidResources {
           enable = true
       }
       withHostTest {
           isIncludeAndroidResources = true
       }
       withDeviceTestBuilder {
           sourceSetTreeName = "test"
       }.configure {
           instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
       }
    }
    
    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.compose.uiTooling)
        }
        commonMain.dependencies {

// Check for the latest stable version matching your Compose Multiplatform version
            implementation("org.jetbrains.androidx.navigation:navigation-compose:2.9.2")
//
//            // Required for modern type-safe navigation routing
//            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.1")
//            implementation(libs.jetbrains.navigation.compose)
//            implementation(libs.kotlinx.serialization.json)
//            implementation(libs.androidx.ui)
//            implementation(libs.androidx.ui.graphics)
//            implementation(libs.androidx.ui.tooling.preview)
//            implementation(libs.androidx.material3)
//            implementation(libs.androidx.material3.adaptive)
//            implementation(libs.androidx.material3.adaptive.navigation.suite)
//            implementation(libs.androidx.material.icons)
//            implementation(libs.androidx.material.icons.extended)
            // Core Compose Multiplatform library for adaptive layouts and window size classes
            implementation("org.jetbrains.compose.material3.adaptive:adaptive:1.3.0-beta02")

            // Optional: For handling multi-pane layouts (like List-Detail Scaffolds)
            implementation("org.jetbrains.compose.material3.adaptive:adaptive-layout:1.3.0-beta02")

            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation("io.ktor:ktor-client-cio:3.0.0") // CIO is a coroutine-based engine
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.11.0")
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.1")
            // Koin
            implementation("io.insert-koin:koin-core:4.1.0")
            implementation("io.insert-koin:koin-compose:4.1.0")
            implementation("io.insert-koin:koin-compose-viewmodel:4.1.0")
// Ktor
            implementation("io.ktor:ktor-client-core:3.0.0")
            implementation("io.ktor:ktor-client-content-negotiation:3.0.0")
            implementation("io.ktor:ktor-serialization-kotlinx-json:3.0.0")

            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.0")
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
}