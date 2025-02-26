rootProject.name = "KotlinMultiplatformPaypalProject"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://k1ra.net/nexus/repository/public/")
    }
}

include(":KotlinMultiplatformPaypalDemo")
include(":KotlinMultiplatformPaypal")
