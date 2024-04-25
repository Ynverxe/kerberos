plugins {
    id("kerberos-base-conventions")
}

dependencies {
    implementation(libs.annotations)
    compileOnly(libs.json)
}