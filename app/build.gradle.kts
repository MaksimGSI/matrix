plugins {
    id("org.jetbrains.kotlin.jvm")
    application
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
}

application {
    mainClass.set("matrix.MainKt")
}

tasks.test {
    useJUnitPlatform()
}