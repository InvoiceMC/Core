plugins {
    id("io.papermc.paperweight.userdev") version "1.5.11"
}

dependencies {
    paperweight.paperDevBundle("1.20.4-R0.1-SNAPSHOT")
    compileOnly(project(path = ":Core-Main", configuration = "shadow"))
    compileOnly(project(path = ":Core-Data", configuration = "shadow"))
}