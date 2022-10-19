plugins {
    `maven-publish`
}

dependencies {
    compileOnly("com.alttd:Galaxy-API:1.19.2-R0.1-SNAPSHOT")
}

tasks {

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