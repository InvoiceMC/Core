repositories {
    maven { url = uri("https://repo.codemc.io/repository/maven-public/") }
}

dependencies {
    implementation("de.tr7zw:item-nbt-api:2.12.2")

    compileOnly(project(":Core-Main"))
}