dependencies {
    implementation("com.github.Azuyamat:Maestro:3.2.1")

    compileOnly(project(path = ":Core-Configs", configuration = "shadow"))
    compileOnly(project(path = ":Core-Crates", configuration = "shadow"))
    compileOnly(project(path = ":Core-Quests", configuration = "shadow"))
    compileOnly(project(path = ":Core-Mining", configuration = "shadow"))
    compileOnly(project(path = ":Core-Data", configuration = "shadow"))
    compileOnly(project(path = ":Core-Main", configuration = "shadow"))
    compileOnly(project(path = ":Core-PMines", configuration = "shadow"))
}