plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.springBoot)
    alias(libs.plugins.springDependencyManagement)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.kotlin.serialization)
    id("io.freefair.lombok") version "8.4"
    id("org.jetbrains.kotlin.plugin.jpa") version "2.3.0"
    application
}
val springCloudVersion by extra("2025.1.1")

group = "org.ninh.instaclone"
version = "1.0.0"
application {
    mainClass.set("org.ninh.instaclone.PostConsumerAppKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

dependencies {
    implementation(libs.logback)
    implementation("org.springframework.cloud:spring-cloud-function-web")
    implementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation(libs.kotlin.testJunit)
    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib"))
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation("org.postgresql:postgresql")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation (platform("com.google.cloud:libraries-bom:26.74.0"))
    implementation("com.google.cloud:google-cloud-pubsub")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.2")
}
dependencyManagement {
    imports { mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion") }
}
