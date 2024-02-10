plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.papermc.paperweight.userdev") version "1.5.11"
    id("me.champeau.jmh") version "0.7.2"
    id("co.uzzu.dotenv.gradle") version "4.0.0" // Dotenv support
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

    maven { url = uri("https://jitpack.io") }
}

dependencies {
    // PaperMC
    paperweight.paperDevBundle("1.20.4-R0.1-SNAPSHOT")

    // Kotlin
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlin:kotlin-reflect") // Kotlin Reflection
    api("org.reflections:reflections:0.9.12") // Reflections

    // Other
    implementation("net.kyori:adventure-text-minimessage:4.14.0")
    implementation("com.github.DebitCardz:mc-chestui-plus:1.4.5")
    implementation("fr.mrmicky:fastboard:2.0.2")
    compileOnly("net.luckperms:api:5.4")

    // Database Stuff
    implementation("com.github.InvoiceMC:Munch:555f98f609")

    implementation("com.github.Azuyamat.Maestro:bukkit:3.0.2") // Maestro command manager

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
        if (!env.DIRECTORY.isPresent) {
            throw IllegalStateException("env.DIRECTORY is not set")
        }
        var dir = env.DIRECTORY.value
        dir += "Core.jar"
        outputJar.set(file(dir))

        doLast {
            val jarFile = file(dir)
            println("ReobfJar: ${jarFile.absolutePath} (${jarFile.length() / (1024 * 1024)} mb)")
        }
    }

    shadowJar {
        relocate("fr.mrmicky.fastboard", "me.outspending.core.relocations.fastboard")
    }

    kotlin {
        jvmToolchain(17)
    }
}
