plugins {
    id("java-library")
    id("org.allaymc.gradle.plugin") version "0.2.1"
}

group = "com.atri.randomteleport"
description = "Wilderness random teleport system for AllayMC servers"
version = "0.1.0"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

allay {
    api = "0.24.0"

    plugin {
        entrance = ".RandomTeleport"
        authors += "daoge_cmd"
        website = "https://github.com/daoge_cmd/RandomTeleport"
    }
}

dependencies {
    compileOnly(group = "org.projectlombok", name = "lombok", version = "1.18.34")
    annotationProcessor(group = "org.projectlombok", name = "lombok", version = "1.18.34")
}
