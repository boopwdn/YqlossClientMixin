@file:Suppress("PropertyName")

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://repo.polyfrost.org/releases")
    }
    plugins {
        val pgtVersion = "0.6.5"
        id("org.polyfrost.multi-version.root") version pgtVersion
    }
}

val mod_name: String by settings

rootProject.name = mod_name
rootProject.buildFileName = "root.gradle.kts"

include(":1.8.9-forge")
project(":1.8.9-forge").apply {
    projectDir = file("versions/1.8.9-forge")
    buildFileName = "../../build.gradle.kts"
}
