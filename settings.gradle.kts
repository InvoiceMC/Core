pluginManagement {
    plugins {
        kotlin("jvm") version "1.9.22"
    }

    repositories {
        gradlePluginPortal()
        maven ("https://repo.papermc.io/repository/maven-public/")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

rootProject.name = "Core"
include("Core-Data")
include("Core-Main")
include("Core-Quests")
include("Core-Configs")
include("Core-Mining")
include("Core-Crates")
include("Core-Bot")
include("Core-Scoreboard")
include("Core-Chat")
include("Core-Commands")
include("Core-Listeners")
include("Core-Holograms")
