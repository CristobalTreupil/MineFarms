pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // 👇 Esta línea ya no es necesaria para SceneView, pero puedes dejarla por otras dependencias
        // maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "MineFarms"
include(":app")