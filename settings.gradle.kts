rootProject.name = "DataLockLib"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven("https://repo.destro.xyz/snapshots") // Altitude - Galaxy
    }
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
}

pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}
