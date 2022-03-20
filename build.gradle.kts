import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.31"

    kotlin("plugin.jpa") version "1.5.31"
    kotlin("plugin.spring") version "1.5.31"
    kotlin("plugin.serialization") version "1.6.10"
    id("org.springframework.boot") version "2.5.5"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"

    id("info.solidsoft.pitest") version "1.7.0"
}

group = "coffee.michel"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11
java.targetCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    // Lang
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // Spring
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springdoc:springdoc-openapi-ui:1.6.6")
    implementation("org.springdoc:springdoc-openapi-kotlin:1.6.6")

    // Other - Web
    implementation("com.nimbusds:nimbus-jose-jwt:9.21")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")

    // Other - Database
    implementation("org.flywaydb:flyway-core")
    runtimeOnly("com.h2database:h2")
    runtimeOnly("org.postgresql:postgresql:42.3.3")

    // Logger with Kotlin API
    implementation("io.github.microutils:kotlin-logging:2.1.21")

    // Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        // Junit 4/3 Support is not needed
        exclude(module = "junit")
        exclude(module = "junit-vintage-engine")
    }
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("com.ninja-squad:springmockk:3.1.1")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

// --- Testing ---

tasks.withType<Test> {
    useJUnitPlatform()
}

/*
Pitest is a mutation testing plugin,
it generates a report which indicates how good your tests are
and may point out some code which needs refactoring.

You can find the report in build/reports/pitest
 */
pitest {
    testSourceSets.add(sourceSets.test)
    mainSourceSets.add(sourceSets.main)

    targetClasses.set(listOf("coffee.michel.*"))
    outputFormats.set(listOf("HTML"))
    timestampedReports.set(false)

    junit5PluginVersion.set("0.15")
    avoidCallsTo.set(
        listOf(
            "kotlin.jvm.internal",
            "mu",
        )
    )

    coverageThreshold.set(80)
    testStrengthThreshold.set(80)
    skipFailingTests.set(true)
    // excludedClasses.set(listOf())
    // excludedTestClasses.set(listOf())
}
