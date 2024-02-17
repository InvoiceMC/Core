dependencies {
    implementation("fr.mrmicky:fastboard:2.0.2")

    compileOnly(project(path = ":Core-Data", configuration = "shadow"))
    compileOnly(project(path = ":Core-Main", configuration = "shadow"))
}