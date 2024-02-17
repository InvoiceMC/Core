plugins {
    id("io.papermc.paperweight.userdev") version "1.5.11"
}

dependencies {
    paperweight.paperDevBundle("1.20.4-R0.1-SNAPSHOT")

    implementation("com.github.DebitCardz:mc-chestui-plus:1.4.8")

    implementation(project(":Core-Data"))
    implementation(project(":Core-Main"))
}

tasks {
    assemble {
        dependsOn(reobfJar)
    }
}