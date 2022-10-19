plugins {
    `maven-publish`
    id("com.github.johnrengelman.shadow")
    id("xyz.jpenilla.run-paper") version "1.0.6"
}

dependencies {
    implementation(project(":api")) // API
    compileOnly("com.alttd:Galaxy-API:1.19.2-R0.1-SNAPSHOT") // Galaxy
}

tasks {

    shadowJar {
        archiveFileName.set("${rootProject.name}-${project.version}.jar")
    }

    build {
        dependsOn(shadowJar)
    }

}
