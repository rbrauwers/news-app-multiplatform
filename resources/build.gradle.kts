plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    id("dev.icerock.mobile.multiplatform-resources")
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    targetHierarchy.default()

    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "resources"
            export(libs.icerock.moko.resources)
            export(libs.icerock.moko.graphics)
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.icerock.moko.resources)
                implementation(libs.icerock.moko.resources.compose)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        val iosX64Main by getting {
            resources.srcDirs("build/generated/moko/iosX64Main/src")
        }
        val iosArm64Main by getting {
            resources.srcDirs("build/generated/moko/iosArm64Main/src")
        }
        val iosSimulatorArm64Main by getting {
            resources.srcDirs("build/generated/moko/iosSimulatorArm64Main/src")
        }
    }
}

android {
    namespace = "com.rbrauwers.newsapp.resources"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }

    sourceSets {
        getByName("main").java.srcDirs("build/generated/moko/androidMain/src")
    }
}

multiplatformResources {
    multiplatformResourcesPackage = "com.rbrauwers.newsapp.resources" // required
    multiplatformResourcesClassName = "MultiplatformResources" // optional, default MR
    //multiplatformResourcesSourceSet = "commonClientMain"  // optional, default "commonMain"
}