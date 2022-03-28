import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    id("org.jetbrains.dokka") version "1.6.10"
    `maven-publish`
    signing
}

group = "com.thizzer.kt-swift-package-collections"
version = "1.0.2-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-core:2.13.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.0")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.13.0")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "16"
}

//

val dokkaHtml by tasks.getting(org.jetbrains.dokka.gradle.DokkaTask::class)

val javadocJar: TaskProvider<Jar> by tasks.registering(Jar::class) {
    dependsOn(dokkaHtml)
    archiveClassifier.set("javadoc")
    from(dokkaHtml.outputDirectory)
}

val sourcesJar: TaskProvider<Jar> by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets["main"].allSource)
}

//

var mavenCentralUsername: String? = System.getenv("MAVEN_CENTRAL_USERNAME")
var mavenCentralPassword: String? = System.getenv("MAVEN_CENTRAL_TOKEN")

publishing {
    repositories {
        maven {
            name = "mavenCentralDefault"

            url = if (version.toString().endsWith("SNAPSHOT")) uri("https://oss.sonatype.org/content/repositories/snapshots/") else uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = mavenCentralUsername
                password = mavenCentralPassword
            }
        }
        maven {
            name = "mavenCentralSnapshot"

            url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
            credentials {
                username = mavenCentralUsername
                password = mavenCentralPassword
            }
        }
        maven {
            name = "mavenCentralRelease"

            url = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = mavenCentralUsername
                password = mavenCentralPassword
            }
        }
    }

    publications {
        create<MavenPublication>("maven") {
            artifactId = "kt-swift-package-collections"
            from(components["kotlin"])
            artifact(javadocJar)
            artifact(sourcesJar)
            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }

            pom {
                name.set("kt-swift-package-collections")
                description.set("Simple Kotlin library for generating Swift package collections.")
                url.set("https://github.com/thizzer/kt-swift-package-collections")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://github.com/thizzer/kt-swift-package-collections/blob/main/LICENSE")
                    }
                    organization {
                        url.set("https://www.thizzer.com")
                        name.set("Thizzer")
                    }
                    developers {
                        developer {
                            name.set("Thys ten Veldhuis")
                            email.set("t.tenveldhuis@gmail.com")
                            organization.set("Thizzer")
                            organizationUrl.set("https://www.thizzer.com")
                        }
                    }
                    scm {
                        connection.set("scm:git:git://github.com/thizzer/kt-swift-package-collections.git")
                        developerConnection.set("scm:git:ssh://github.com:thizzer/kt-swift-package-collections.git")
                        url.set("https://github.com/thizzer/kt-swift-package-collections/tree/main")
                    }
                }
            }
        }
    }
}

signing {
    sign(publishing.publications["maven"])
}