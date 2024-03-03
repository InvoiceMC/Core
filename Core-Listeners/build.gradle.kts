dependencies {
    compileOnly(project(path = ":Core-Data", configuration = "shadow"))
    compileOnly(project(path = ":Core-Main", configuration = "shadow"))
    compileOnly(project(path = ":Core-Chat", configuration = "shadow"))
    compileOnly(project(path = ":Core-Mining", configuration = "shadow"))
    compileOnly(project(path = ":Core-Quests", configuration = "shadow"))
    compileOnly(project(path = ":Core-Crates", configuration = "shadow"))
    compileOnly(project(path = ":Core-PMines", configuration = "shadow"))
    compileOnly(project(path = ":Core-Scoreboard", configuration = "shadow"))
}