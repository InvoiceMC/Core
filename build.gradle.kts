plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("me.champeau.jmh") version "0.7.2"
    id("co.uzzu.dotenv.gradle") version "4.0.0" // Dotenv support
    kotlin("jvm") version "2.0.0-Beta4"
    java
}

allprojects {
    apply(plugin = "kotlin")

    group = "me.outspending"
    version = "0.0.1"
    description = "Core plugin for the server Invoice"

    repositories {
        mavenCentral()
        maven { url = uri("https://repo.papermc.io/repository/maven-public/") }
        maven { url = uri("https://oss.sonatype.org/content/groups/public/") }
        maven { url = uri("https://jitpack.io") }
    }

    dependencies {
        compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
        implementation("net.kyori:adventure-text-minimessage:4.14.0")
        implementation("org.reflections:reflections:0.10.2")
    }
}

dependencies {
    compileOnly("net.luckperms:api:5.4") // LuckPerms API

    // Modules
    compileOnly(project(":Core-Bot"))
    compileOnly(project(":Core-Main"))
    compileOnly(project(":Core-Data"))
    compileOnly(project(":Core-Configs"))
    compileOnly(project(":Core-Crates"))
    compileOnly(project(":Core-Commands"))
    compileOnly(project(":Core-Scoreboard"))
    compileOnly(project(":Core-Listeners"))

    // JMH
    jmh("commons-io:commons-io:2.7")
    jmh("org.openjdk.jmh:jmh-core:1.37")
    jmhAnnotationProcessor("org.openjdk.jmh:jmh-generator-annprocess:1.37")
}

tasks {
    javadoc { options.encoding = Charsets.UTF_8.name() }

    processResources {
        val props =
            mapOf(
                "name" to project.name,
                "version" to project.version,
                "description" to project.description,
                "apiVersion" to "1.20"
            )
        inputs.properties(props)
        filteringCharset = Charsets.UTF_8.name()
        filesMatching("plugin.yml") { expand(props) }
    }

    shadowJar {
        relocate("fr.mrmicky.fastboard", "me.outspending.core.relocations.fastboard")
        relocate("de.tr7zw.changeme.nbtapi", "me.outspending.core.relocations.nbtapi")

        if (!env.DIRECTORY.isPresent) {
            throw IllegalStateException("DIRECTORY environment variable is not set")
        }

        val directory = env.DIRECTORY.value
        destinationDirectory.set(file(directory))
    }

    kotlin { jvmToolchain(17) }
}
