plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.papermc.paperweight.userdev") version "1.5.11" apply false
    id("me.champeau.jmh") version "0.7.2"
    id("co.uzzu.dotenv.gradle") version "4.0.0" // Dotenv support
    kotlin("jvm") version "2.0.0-Beta4"
    java
}

allprojects {
    apply(plugin = "kotlin")
    apply(plugin = "com.github.johnrengelman.shadow")

    group = "me.outspending"
    version = "0.0.1"
    description = "Core plugin for the server Invoice"

    repositories {
        mavenCentral()
        maven { url = uri("https://repo.papermc.io/repository/maven-public/") }
        maven { url = uri("https://oss.sonatype.org/content/groups/public/") }
        maven { url = uri("https://repo.codemc.io/repository/maven-public/") }
        maven { url = uri("https://jitpack.io") }
    }

    dependencies {
        compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")

        api("org.reflections:reflections:0.9.8")

        implementation("net.kyori:adventure-text-minimessage:4.14.0")
        implementation("de.tr7zw:item-nbt-api:2.12.2")
    }

    tasks {
        kotlin {
            jvmToolchain(17)
        }
    }
}

dependencies {
    compileOnly("net.luckperms:api:5.4") // LuckPerms API

    // Modules
    implementation(project(":Core-Bot"))
    implementation(project(":Core-Chat"))
    implementation(project(":Core-Commands"))
    implementation(project(":Core-Configs"))
    implementation(project(":Core-Crates"))
    implementation(project(":Core-Data"))
    implementation(project(":Core-Listeners"))
    implementation(project(":Core-Main"))
    implementation(project(":Core-Quests"))
    implementation(project(":Core-Scoreboard"))
    implementation(project(":Core-PMines"))

    // Reobf Modules (These are only modules that contain paperweight)
    implementation(project(path = ":Core-Mining",    configuration = "reobf"))
    implementation(project(path = ":Core-Holograms", configuration = "reobf"))
    implementation(project(path = ":Core-Heads",     configuration = "reobf"))

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
        archiveFileName.set("${project.name}.jar")

        relocate("fr.mrmicky.fastboard", "me.outspending.core.relocations.fastboard")
        relocate("de.tr7zw.changeme.nbtapi", "me.outspending.core.relocations.nbtapi")

        if (!env.DIRECTORY.isPresent) {
            throw IllegalStateException("DIRECTORY environment variable is not set")
        }

        val directory = env.DIRECTORY.value
        destinationDirectory.set(file(directory))
    }
}