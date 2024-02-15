plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.papermc.paperweight.userdev") version "1.5.11"
    id("me.champeau.jmh") version "0.7.2"
    id("co.uzzu.dotenv.gradle") version "4.0.0" // Dotenv support
    kotlin("jvm") version "2.0.0-Beta4"
    java
}

group = "me.outspending"

version = "0.0.1"

description = "Core plugin for the server Invoice"

repositories {
    mavenCentral()

    maven { url = uri("https://repo.papermc.io/repository/maven-public/") }

    maven { url = uri("https://oss.sonatype.org/content/groups/public/") }

    maven { url = uri("https://repo.dmulloy2.net/repository/public/") }

    maven { url = uri("https://repo.papermc.io/repository/maven-public/") }

    maven { url = uri("https://jitpack.io") }

    maven { url = uri("https://repo.codemc.io/repository/maven-public/") }
}

dependencies {
    // PaperMC
    paperweight.paperDevBundle("1.20.4-R0.1-SNAPSHOT")

    implementation(kotlin("stdlib-jdk8")) // Kotlin Standard Library
    implementation("org.jetbrains.kotlin:kotlin-reflect") // Kotlin Reflection

    implementation("net.kyori:adventure-text-minimessage:4.14.0") // Adventure Text
    implementation("com.github.DebitCardz:mc-chestui-plus:1.4.8") // Inventory API
    implementation("fr.mrmicky:fastboard:2.0.2") // Our Scoreboard API
    compileOnly("net.luckperms:api:5.4") // LuckPerms API

    implementation("com.github.InvoiceMC:Munch:c923e5f764") // Database Manager

    implementation("com.github.Azuyamat:Maestro:3.2.1") // Maestro command manager

    implementation("net.dv8tion:JDA:5.0.0-beta.20") // JDA

    // JMH
    jmh("commons-io:commons-io:2.7")
    jmh("org.openjdk.jmh:jmh-core:1.37")
    jmhAnnotationProcessor("org.openjdk.jmh:jmh-generator-annprocess:1.37")

    // NBT-api
    implementation("de.tr7zw:item-nbt-api:2.12.2")
}

tasks {
    assemble { dependsOn(reobfJar) }

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
        relocate("de.tr7zw.changeme.nbtapi", "me.outspending.core.relocations.nbtapi")
    }

    kotlin { jvmToolchain(17) }
}
