import java.io.ByteArrayOutputStream

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.0"
    id("maven-publish")
}

allprojects {
    group = "com.alttd"
    version = "1.0.0-SNAPSHOT"
    description = "Altitude DataLock Library."
}

subprojects {
    apply<JavaLibraryPlugin>()
    apply(plugin = "maven-publish")

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    }
}

tasks {
    jar {
        enabled = false
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }

    repositories{
        maven {
            name = "maven"
            url = uri("https://repo.destro.xyz/snapshots")
            credentials(PasswordCredentials::class)
        }
    }
}

dependencies {
    compileOnly("com.alttd:Galaxy-API:1.19.2-R0.1-SNAPSHOT")
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
