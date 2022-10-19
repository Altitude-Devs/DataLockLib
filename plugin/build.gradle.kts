import java.io.ByteArrayOutputStream

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

    shadowJar {
        archiveFileName.set("${rootProject.name}.jar")
    }

    build {
        dependsOn(shadowJar)
    }

}

bukkit {
    name = project.name
    main = "$group.${project.name}"
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
