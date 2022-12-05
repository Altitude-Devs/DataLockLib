plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.0"
    id("maven-publish")
}

allprojects {
    group = "com.alttd.datalock"
    version = "1.1.0-SNAPSHOT"
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
        enabled = true
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
