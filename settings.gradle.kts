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
        maven { url=uri("https://jitpack.io") }
        maven {url = uri("https://maven.aliyun.com/nexus/content/repositories/releases/")}
        maven {url=uri("https://developer.huawei.com/repo/")}
    }
}

rootProject.name = "CommonTemplate"
include(":app")
include(":retrofit_net")
include(":frame")
include(":libUCrop")
