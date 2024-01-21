plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.papermc.paperweight.userdev") version "1.5.11"
    id("me.champeau.jmh") version "0.7.2"
    kotlin("jvm") version "2.0.0-Beta2"
    java
}

group = "me.outspending"
version = "0.0.1"

repositories {
    mavenCentral()

    maven { url = uri("https://repo.papermc.io/repository/maven-public/") }

    maven { url = uri("https://oss.sonatype.org/content/groups/public/") }

    maven { url = uri("https://repo.dmulloy2.net/repository/public/") }

    maven { url = uri("https://repo.papermc.io/repository/maven-public/") }

    maven { url = uri("https://repo.sparky983.me/releases") }
}

dependencies {
    // PaperMC
    paperweight.paperDevBundle("1.20.4-R0.1-SNAPSHOT")

    // Kotlin
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0-RC2")

    // Other
    implementation("net.kyori:adventure-text-minimessage:4.14.0")
    implementation("me.sparky983:vision-paper:1.0.1")
    implementation("fr.mrmicky:fastboard:2.0.2")
    compileOnly("net.luckperms:api:5.4")

    // FAWE
    implementation(platform("com.intellectualsites.bom:bom-newest:1.38"))
    compileOnly("com.fastasyncworldedit:FastAsyncWorldEdit-Core")
    compileOnly("com.fastasyncworldedit:FastAsyncWorldEdit-Bukkit") { isTransitive = false }

    // JMH
    jmh("commons-io:commons-io:2.7")
    jmh("org.openjdk.jmh:jmh-core:1.37")
    jmhAnnotationProcessor("org.openjdk.jmh:jmh-generator-annprocess:1.37")
}

tasks {

    processResources {
        val props =
            mapOf(
                "version" to project.version,
            )
        inputs.properties(props)
        filteringCharset = Charsets.UTF_8.name()
        filesMatching("plugin.yml") {
            expand(props)
        }
    }

    reobfJar {
        outputJar.set(file("E:\\Servers\\Testing\\plugins\\Core.jar"))
    }

    shadowJar {
        relocate("fr.mrmicky.fastboard", "me.outspending.core.relocations.fastboard")
    }

    kotlin {
        jvmToolchain(17)
    }
}
