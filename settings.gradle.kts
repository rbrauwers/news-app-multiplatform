rootProject.name = "NewsMultiplatform"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

include(":composeApp")
include(":core:auth")
include(":core:common")
include(":core:data")
include(":core:designsystem")
include(":core:database")
include(":core:model")
include(":core:network")
include(":features:headlines")
include(":features:sources")
include(":resources")
