
pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    plugins {
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "ComFin"

include(":app")
include(":core:test")
include(":core:ui")
include(":core:navigation")
include(":data:common")
include(":data:home")
include(":data:operations")
include(":data:misc")
include(":feature:home")
include(":feature:operations")
include(":feature:settings")
include(":feature:misc")
