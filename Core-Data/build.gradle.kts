dependencies {
    implementation("com.github.InvoiceMC:Munch:76200f8da3")

    compileOnly(project(path = ":Core-Holograms", configuration = "shadow"))
    compileOnly(project(path = ":Core-Main", configuration = "shadow"))
}