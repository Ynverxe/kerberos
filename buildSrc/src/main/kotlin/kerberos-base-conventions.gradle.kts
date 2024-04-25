plugins {
    id("java")
    id("java-library")
    id("maven-publish")
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group
                .toString()
                .substring(1, project.group.toString().length - 1)

            artifactId = project.name
            version = project.version.toString()

            from(components["java"])
        }
    }
}