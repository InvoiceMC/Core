dependencies {
    implementation("com.github.InvoiceMC:Munch:c923e5f764")

    compileOnly(project(path = ":Core-Main", configuration = "shadow"))
}