dependencies {
    implementation("com.github.InvoiceMC:Munch:74846797d5")

    compileOnly(project(path = ":Core-Holograms", configuration = "shadow"))
    compileOnly(project(path = ":Core-Main", configuration = "shadow"))
}