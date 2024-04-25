plugins {
    id("kerberos-base-conventions")
}

dependencies {
    implementation(project(":kerberos-universal"))

    implementation(libs.annotations)

    compileOnly(libs.hikariCP)

    testImplementation(libs.h2)
}