plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.sqlDelight)
}

kotlin {
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
            baseName = "database"
            isStatic = true
            compilation.kotlinOptions.freeCompilerArgs += arrayOf("-linker-options", "-lsqlite3")
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.sql.delight.coroutines)
            implementation(libs.sql.delight.runtime)

            implementation(dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)

            implementation(project(":core:model"))
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        androidMain.dependencies {
            implementation(libs.sql.delight.android.driver)
        }

        iosMain.dependencies {
            implementation(libs.sql.delight.native.driver)

            // Needed due a transitive dependency conflict btw SQLDelight and Koin
            // https://github.com/cashapp/sqldelight/issues/4357
            implementation(libs.touchlab.stately.common)
        }
    }
}

android {
    namespace = "com.rbrauwers.newsapp.database"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }
}

sqldelight {
    databases {
        create("NewsMultiplatformDatabase") {
            packageName.set("com.rbrauwers.newsapp.database")
        }
    }

    linkSqlite = true
}