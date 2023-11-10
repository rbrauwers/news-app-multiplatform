plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsCompose)
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
            baseName = "sources"
        }
    }

    sourceSets {
        androidMain.dependencies {
            val composeBom = dependencies.platform(libs.androidx.compose.bom)
            implementation(composeBom)
            implementation(libs.androidx.compose.ui)
            implementation(libs.androidx.compose.ui.tooling.preview)
            implementation(libs.androidx.activity.compose)
        }

        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)

                implementation(dependencies.platform(libs.koin.bom))
                implementation(libs.koin.compose)
                implementation(libs.koin.core)

                implementation(libs.voyager.koin)
                implementation(libs.voyager.navigator)
                implementation(libs.voyager.tab.navigator)

                implementation(project(":core:common"))
                implementation(project(":core:data"))
                implementation(project(":core:model"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }
    }
}

android {
    namespace = "com.rbrauwers.newsapp.sources"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }
}
