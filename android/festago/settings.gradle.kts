pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://devrepo.kakao.com/nexus/content/groups/public/") }
    }
}
rootProject.name = "festago"
// app
include(":app")

// common
include(":common")

// domain
include(":domain")

// presentation
include(":presentation-legacy")
include(":presentation")

// dat
include(":data-legacy")
