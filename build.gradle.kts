plugins {
    kotlin("jvm") version "2.2.20"
    kotlin("plugin.serialization") version "2.0.21"
    id("com.gradleup.shadow") version "8.3.0"
    id("xyz.jpenilla.run-paper") version "2.3.1"
}

group = "ru.spfort"
version = "1.0"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "papermc-repo"
    }
    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "papermc-repo"
    }
    maven("https://maven.pkg.github.com/FiLKoNd/paperKtLib") {
        credentials {
            username = System.getenv("GITHUB_USERNAME")
            password = System.getenv("GITHUB_TOKEN")
        }
    }
    maven {
        name = "CodeMC"
        url = uri("https://repo.codemc.io/repository/maven-public/")
    }
    maven("https://repo.dmulloy2.net/repository/public/")
    maven("https://repo.triumphteam.dev/snapshots")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
    compileOnly("de.tr7zw:item-nbt-api-plugin:2.15.2")
    compileOnly("com.comphenix.protocol:ProtocolLib:5.3.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
    val paperKtLibVersion = "1.3.7"
    implementation("com.filkond:paperktlib-adventure:$paperKtLibVersion")
    implementation("com.filkond:paperktlib-config:$paperKtLibVersion")
    implementation("com.filkond:paperktlib-paper:$paperKtLibVersion")
    implementation("dev.triumphteam:triumph-gui-paper-kotlin:4.0.0-SNAPSHOT")
    implementation("dev.triumphteam:triumph-cmd-core:2.0.0-SNAPSHOT")
    implementation("dev.triumphteam:triumph-cmd-bukkit:2.0.0-SNAPSHOT")
    implementation(kotlin("reflect"))
}

tasks {
    runServer {
        // Configure the Minecraft version for our task.
        // This is the only required configuration besides applying the plugin.
        // Your plugin's jar (or shadowJar if present) will be used automatically.
        minecraftVersion("1.21.4")
        downloadPlugins {
            url("https://github.com/dmulloy2/ProtocolLib/releases/download/5.4.0/ProtocolLib.jar")
            url("https://cdn.modrinth.com/data/nfGCP9fk/versions/AcWj8a6A/item-nbt-api-plugin-2.15.2.jar")
        }
    }
}

val targetJavaVersion = 21
kotlin {
    jvmToolchain(targetJavaVersion)
}

tasks.build {
    dependsOn("shadowJar")
}

tasks.processResources {
    val props = mapOf("version" to version)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("plugin.yml") {
        expand(props)
    }
}
