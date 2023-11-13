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
include(":core:database")
include(":core:network")
include(":core:model")
include(":features:headlines")
include(":core:data")
include(":core:common")
include(":features:sources")
include(":core:designsystem")
include(":resources")
