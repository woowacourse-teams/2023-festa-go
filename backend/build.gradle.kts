plugins {
    id("java")
    id("org.springframework.boot") version "3.1.4"
    id("io.spring.dependency-management") version "1.1.0"
}

group = "com"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

springBoot {
    buildInfo()
}

configurations.all {
    exclude(group = "commons-logging", module = "commons-logging")
}

val swaggerVersion = "2.0.2"
val restAssuredVersion = "5.3.0"
val jjwtVersion = "0.11.5"
val logbackSlackAppenderVersion = "1.4.0"
val cucumberVersion = "7.13.0"
val firebaseVersion = "8.1.0"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:${swaggerVersion}")

    // Spring Security
    implementation("org.springframework.security:spring-security-crypto")

    runtimeOnly("com.h2database:h2")
    runtimeOnly("com.mysql:mysql-connector-j")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.rest-assured:rest-assured:${restAssuredVersion}")

    // JWT
    implementation("io.jsonwebtoken:jjwt-api:${jjwtVersion}")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:${jjwtVersion}")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:${jjwtVersion}")

    // Logback Slack Alarm
    implementation("com.github.maricn:logback-slack-appender:${logbackSlackAppenderVersion}")

    // Cucumber
    testImplementation("io.cucumber:cucumber-java:${cucumberVersion}")
    testImplementation("io.cucumber:cucumber-spring:${cucumberVersion}")
    testImplementation("io.cucumber:cucumber-junit-platform-engine:${cucumberVersion}")
    testImplementation("org.junit.platform:junit-platform-suite")

    // Querydsl
    implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
    annotationProcessor("com.querydsl:querydsl-apt:5.0.0:jakarta")
    annotationProcessor("jakarta.annotation:jakarta.annotation-api")
    annotationProcessor("jakarta.persistence:jakarta.persistence-api")

    // Flyway
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-mysql")

    // Firebase
    implementation("com.google.firebase:firebase-admin:${firebaseVersion}")

    // Lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")

    // Micrometer
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")
}

tasks.test {
    useJUnitPlatform()
}

// Querydsl 폴더 지정
val querydslDir = file("build/generated/querydsl")

// querydsl QClass 파일 생성 위치를 지정
tasks.compileJava {
    options.generatedSourceOutputDirectory.set(querydslDir)
}
