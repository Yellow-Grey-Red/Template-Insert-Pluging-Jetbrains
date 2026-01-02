plugins {
    id("java")
    id("org.jetbrains.intellij") version "1.5.2"
    id("org.openjfx.javafxplugin") version "0.0.13"

}

group = "com.nfym"
version = "1.0.0"

repositories {
    mavenLocal()
    maven{
        url=uri("https://maven.aliyun.com/repository/public/")
    }
    mavenCentral()
    google()
}
// Configure JavaFX
javafx {
    version = "11"
    modules = listOf("javafx.controls", "javafx.fxml", "javafx.graphics")
}

// Configure Gradle IntelliJ Plugin - read more: https://github.com/JetBrains/gradle-intellij-plugin
intellij {
    version.set("2021.2")
    type.set("IC") // Target IDE Platform

    plugins.set(listOf("java", "org.jetbrains.plugins.gradle"))
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.0")
    implementation("org.json:json:20210307")
}

tasks.create("UserShadowJar",Jar::class){
    dependsOn("build")
    group="build"
    description=""
    duplicatesStrategy=org.gradle.api.file.DuplicatesStrategy.EXCLUDE

    val c=sourceSets.main.get().output
    from(c)
    val d=configurations.runtimeClasspath.get().map { f -> zipTree(f) }
    from(d)
}


tasks {
    
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "11"
        targetCompatibility = "11"
        options.encoding="UTF-8"
    }

    withType<JavaExec>{
        jvmArgs = listOf(
            "-Xmx512m",
            "-Dfile.encoding=UTF-8",
            "-Dsun.stdout.encoding=UTF-8",
            "-Dsun.stderr.encoding=UTF-8",
//            "-Xlog:class+load=trace:stdout:tags"
        )
        standardOutput = System.out
        errorOutput = System.err
    }
    patchPluginXml {
        sinceBuild.set("212")
        untilBuild.set("222.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
    

}
