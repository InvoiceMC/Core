dependencies {
    implementation(platform("com.intellectualsites.bom:bom-newest:1.42"))
    compileOnly("com.fastasyncworldedit:FastAsyncWorldEdit-Core")
    compileOnly("com.fastasyncworldedit:FastAsyncWorldEdit-Bukkit") { isTransitive = false }

    compileOnly(project(path = ":Core-Main", configuration = "shadow"))
    compileOnly(project(path = ":Core-Data", configuration = "shadow"))
    compileOnly(project(path = ":Core-Holograms", configuration = "shadow"))
}