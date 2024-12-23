plugins {
    id("java")
}

group = "com.yinlin"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation(files("./lib/gdx.jar"))
    implementation(files("./lib/gdx-backend-lwjgl.jar"))
    implementation(files("./lib/gdx-backend-lwjgl-natives.jar"))
    implementation(files("./lib/gdx-freetype.jar"))
    implementation(files("./lib/gdx-freetype-natives.jar"))
    implementation(files("./lib/gdx-natives.jar"))
    implementation(files("./lib/gdx-sources.jar"))
    implementation(files("./lib/gdx-tools.jar"))
    implementation(files("./lib/tween-engine-api.jar"))
    implementation(files("./lib/tween-engine-api-sources.jar"))
    // https://mvnrepository.com/artifact/org.json/json
    implementation("org.json:json:20240303")

}

tasks.test {
    useJUnitPlatform()
}

