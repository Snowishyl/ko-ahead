val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val mapstructVersion = "1.6.0.Beta1"
val jimmerVersion = "0.8.46"


plugins {
    val kotlinVersion = "1.9.21"
    id("io.ktor.plugin") version "2.3.10"
    id("org.jetbrains.kotlin.kapt") version kotlinVersion
    kotlin("jvm") version "1.9.23"
}

group = "yxyl.com"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
    maven { url = uri("https://repo.spring.io/snapshot") }
    gradlePluginPortal()
}

dependencies {
    implementation("io.ktor:ktor-server-auto-head-response-jvm")
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-cors-jvm")
    implementation("io.ktor:ktor-server-openapi")
//    implementation("org.jetbrains.kotlinx:kotlinx-validation-core:$kotlin_version")
    implementation("io.ktor:ktor-server-call-logging-jvm")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-serialization-jackson-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-server-config-yaml:2.3.10")
    implementation("org.kodein.di:kodein-di:7.20.2")
    testImplementation("io.ktor:ktor-server-tests-jvm")
    implementation("org.mapstruct:mapstruct:$mapstructVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    implementation(kotlin("stdlib-jdk8"))

    kapt("org.mapstruct:mapstruct-processor:$mapstructVersion")
    kapt("org.babyfish.jimmer:jimmer-mapstruct-apt:$jimmerVersion")

}

kotlin {
    sourceSets.all {
        languageSettings {
            languageVersion = "2.0"
        }
    }
}
