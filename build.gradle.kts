import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar;

plugins {
    java
    id("com.github.johnrengelman.shadow") version "7.1.0"
}

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://repo.dmulloy2.net/nexus/repository/public/")
    maven("https://repo.codemc.org/repository/maven-public/")
    maven("https://jitpack.io")
    maven("https://repo.maven.apache.org/maven2/")
}

dependencies {
    implementation("me.mattstudios.utils:matt-framework:1.4")
    implementation("de.tr7zw:item-nbt-api-plugin:2.8.0")
    compileOnly("com.comphenix.protocol:ProtocolLib:4.7.0")
    compileOnly("io.papermc.paper:paper-api:1.17.1-R0.1-SNAPSHOT")
}

group = "com.github.klyser8"
description = "MobMadness"
version = "1.0.0"

java.sourceCompatibility = JavaVersion.VERSION_16

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}

tasks.named<ShadowJar>("shadowJar") {
    relocate("me.mattstudios.mf", "com.github.klyser8.mf")
}
