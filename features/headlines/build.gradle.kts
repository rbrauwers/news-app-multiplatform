plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsCompose)
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
            baseName = "headlines"
            export(libs.icerock.moko.resources)
            export(libs.icerock.moko.graphics)
        }
    }

    sourceSets {
        androidMain.dependencies {
            //val composeBom = dependencies.platform(libs.androidx.compose.bom)
            //implementation(composeBom)
            //implementation(libs.androidx.compose.ui)
            //implementation(libs.androidx.compose.ui.tooling.preview)
            //implementation(libs.androidx.activity.compose)
        }

        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)
                implementation(compose.materialIconsExtended)

                implementation(dependencies.platform(libs.koin.bom))
                implementation(libs.koin.compose)
                implementation(libs.koin.core)

                implementation(libs.kotlinx.collections.immutable)

                implementation(libs.kamel)

                implementation(libs.icerock.moko.resources)
                implementation(libs.icerock.moko.resources.compose)

                implementation(libs.decompose)
                implementation(libs.decompose.extensions)

                implementation(project(":core:common"))
                implementation(project(":core:data"))
                implementation(project(":core:designsystem"))
                implementation(project(":core:model"))
                implementation(project(":resources"))
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        /*
        val iosX64Main by getting {
            resources.srcDirs("build/generated/moko/iosX64Main/src")
        }
        val iosArm64Main by getting {
            resources.srcDirs("build/generated/moko/iosArm64Main/src")
        }
        val iosSimulatorArm64Main by getting {
            resources.srcDirs("build/generated/moko/iosSimulatorArm64Main/src")
        }
         */

        /*
        val androidMain by getting
        val androidUnitTest by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by getting {
            dependencies {
                implementation(libs.icerock.moko.resources)
                implementation(libs.icerock.moko.resources.compose)
            }
            //dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        */
    }
}

android {
    namespace = "com.rbrauwers.newsapp.headlines"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
    }

    dependencies {
        implementation(compose.preview)
        debugImplementation(compose.uiTooling)
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        //kotlinCompilerExtensionVersion = libs.versions.compose.compiler.toString()
        kotlinCompilerExtensionVersion = "1.5.3"
    }

    sourceSets {
        getByName("main").java.srcDirs("build/generated/moko/androidMain/src")
    }
}

/*
multiplatformResources {
    multiplatformResourcesPackage = "com.rbrauwers.newsapp.headlines" // required
    multiplatformResourcesClassName = "MultiplatformResources" // optional, default MR
    //multiplatformResourcesSourceSet = "commonClientMain"  // optional, default "commonMain"
}
 */