plugins {
    kotlin("jvm") version "1.9.0"
    application
}

group = "me.ritom"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "MainKt"
    }
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("com.google.api-client:google-api-client:1.23.0")
    implementation("com.google.oauth-client:google-oauth-client-jetty:1.23.0")
    implementation("com.google.apis:google-api-services-youtube:v3-rev222-1.25.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.1")
    implementation(files("libs/Youtube-Downloader.jar"))

}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}

application {
    mainClass.set("MainKt")
}