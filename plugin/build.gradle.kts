import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import java.net.URL

plugins {
    `maven-publish`
    id("com.github.johnrengelman.shadow")
    id("net.minecrell.plugin-yml.bukkit") version "0.5.1"
    id("xyz.jpenilla.run-paper") version "1.0.6"
}

dependencies {
    implementation(project(":api")) // API
    compileOnly("com.alttd:Galaxy-API:1.19.2-R0.1-SNAPSHOT") // Galaxy
}

tasks {

    jar {
        enabled = false
    }

    shadowJar {
        archiveFileName.set("${rootProject.name}.jar")
    }

    build {
        dependsOn(shadowJar)
    }

    runServer {
        val dir = File(System.getProperty("user.home") + "/share/devserver/")
        if (!dir.parentFile.exists()) {
            dir.parentFile.mkdirs()
        }
        runDirectory.set(dir)

        val fileName = "/galaxy.jar"
        val file = File(dir.path + fileName)

        if (!file.parentFile.exists()) {
            file.parentFile.mkdirs()
        }
        if (!file.exists()) {
            download("https://repo.destro.xyz/snapshots/com/alttd/Galaxy-Server/Galaxy-paperclip-1.19.2-R0.1-SNAPSHOT-reobf.jar", file)
        }
        serverJar(file)
        minecraftVersion("1.19.2")
    }

}

bukkit {
    name = rootProject.name
    main = "$group.${rootProject.name}"
    version = gitCommit()
    apiVersion = "1.19"
    authors = listOf("Teriuihi")
}

fun gitCommit(): String {
    val os = ByteArrayOutputStream()
    project.exec {
        isIgnoreExitValue = true
        commandLine = "git rev-parse --short HEAD".split(" ")
        standardOutput = os
    }
    return String(os.toByteArray()).trim()
}

fun download(link: String, path: File) {
    URL(link).openStream().use { input ->
        FileOutputStream(path).use { output ->
            input.copyTo(output)
        }
    }
}