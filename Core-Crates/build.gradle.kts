dependencies {
    compileOnly(project(path = ":Core-Holograms", configuration = "shadow"))
    compileOnly(project(path = ":Core-Main", configuration = "shadow"))
    implementation("com.github.DebitCardz:mc-chestui-plus:1.4.8")
}