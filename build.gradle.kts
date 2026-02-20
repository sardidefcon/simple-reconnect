version = "1.0"

plugins {
    id("java")
    alias(libs.plugins.shadow) apply true
    alias(libs.plugins.runVelocity)
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly(libs.velocity.api)
    annotationProcessor(libs.velocity.api)

    compileOnly(libs.litebans.api)
    compileOnly(libs.luckperms.api)

    // bStats metrics for Velocity
    implementation("org.bstats:bstats-velocity:3.1.0")

    // SnakeYAML 1.33 empaquetado y reubicado para evitar conflicto con el SnakeYAML del servidor (Velocity/Paper)
    implementation(libs.snakeyaml)
    implementation(libs.storage.yaml) {
        exclude(group = "org.yaml", module = "snakeyaml")
    }
    implementation(libs.storage.mysql)
    implementation(libs.storage.maria)
    implementation(libs.storage.sqlite)
    implementation(libs.storage.postgresql)
    implementation(libs.storage.hikari)
}

sourceSets["main"].resources.srcDir("src/resources/")

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks {
    build {
        dependsOn(shadowJar)
    }
    shadowJar {
        archiveBaseName.set("SimpleReconnect")
        mergeServiceFiles()
        // Reubicar SnakeYAML para que el plugin use su propia copia y no la del servidor
        relocate("org.yaml.snakeyaml", "com.simpleplugins.reconnect.lib.org.yaml.snakeyaml")
        // Reubicar bStats para evitar conflictos con otros plugins
        relocate("org.bstats", "com.simpleplugins.reconnect.bstats")
    }
    runVelocity {
        velocityVersion(libs.versions.velocity.get())
    }
}