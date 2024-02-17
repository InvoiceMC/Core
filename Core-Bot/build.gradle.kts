dependencies {
    implementation("net.dv8tion:JDA:5.0.0-beta.20")

    compileOnly(project(path = ":Core-Main", configuration = "shadow"))
    compileOnly(project(path = ":Core-Configs", configuration = "shadow"))
}