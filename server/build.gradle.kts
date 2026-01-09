plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.springBoot)
    alias(libs.plugins.springDependencyManagement)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.kotlin.serialization)
    application
}

group = "org.ninh.instaclone"
version = "1.0.0"
application {
    mainClass.set("org.ninh.instaclone.ApplicationKt")
    
    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

dependencies {
    implementation(projects.shared)
    implementation(libs.logback)
    //implementation(libs.ktor.serverCore)
    //implementation(libs.ktor.serverNetty)
    //testImplementation(libs.ktor.serverTestHost)
    testImplementation(libs.kotlin.testJunit)
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib"))
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}