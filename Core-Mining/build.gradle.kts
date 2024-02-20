plugins {
    id("io.papermc.paperweight.userdev") version "1.5.11"
}

dependencies {
    paperweight.paperDevBundle("1.20.4-R0.1-SNAPSHOT")

    implementation("com.github.DebitCardz:mc-chestui-plus:1.4.8")

    compileOnly(project(path = ":Core-PMines", configuration = "shadow"))
    compileOnly(project(path = ":Core-Data", configuration = "shadow"))
    compileOnly(project(path = ":Core-Main", configuration = "shadow"))
}