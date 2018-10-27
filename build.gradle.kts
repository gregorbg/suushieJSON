import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.js.resolve.JsPlatform

plugins {
    kotlin("jvm") version "1.2.71"
    antlr
    `maven-publish`
}

group = "com.suushiemaniac"
version = "2.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    antlr("org.antlr:antlr4:4.7+")
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    create<Jar>("sourcesJar") {
        classifier = "sources"

        from(java.sourceSets["main"].output)
        dependsOn("classes")
    }

    getByName<AntlrTask>("generateGrammarSource") {
        arguments = arguments + listOf("-visitor", "-no-listener", "-package", "com.suushiemaniac.lang.json.antlr")
    }

    getByName<KotlinCompile>("compileKotlin") {
        dependsOn("generateGrammarSource")
    }
}

kotlin.sourceSets {
    getByName("main") {
        kotlin.exclude("Main.kt")
    }
}

publishing.publications {
    create<MavenPublication>("mavenJava") {
        from(components["java"])
        artifact(tasks["sourcesJar"])
    }
}
