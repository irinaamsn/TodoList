plugins {
	java
	id("org.springframework.boot") version "3.1.5"
	id("io.spring.dependency-management") version "1.1.3"
	jacoco
}

group = "fintech"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

jacoco {
	toolVersion = "0.8.9"
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	//data
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	//security
	implementation("org.springframework.boot:spring-boot-starter-security")
	//thymeleaf
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity6")
	//validator
	implementation("org.springframework.boot:spring-boot-starter-validation")
	//boot
	implementation("org.springframework.boot:spring-boot-starter-web")
	//liquibase
	implementation("org.liquibase:liquibase-core")
	//lombok
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	//postgres
	runtimeOnly("org.postgresql:postgresql")
	//resilience4j
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-aop")
	implementation("io.github.resilience4j:resilience4j-spring-boot3:2.0.2")
	//swagger
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.4")
	//test
	testImplementation("io.rest-assured:rest-assured:5.3.1")
	testImplementation("org.testcontainers:testcontainers:1.18.3")
	testImplementation("org.testcontainers:junit-jupiter:1.18.3")
	testImplementation("org.springframework.boot:spring-boot-testcontainers")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.test {
	finalizedBy(tasks.jacocoTestReport)
}
tasks.jacocoTestReport {
	reports {
		xml.required = false
		csv.required = false
		html.outputLocation = layout.buildDirectory.dir("jacocoHtml")
	}
}

tasks.register<Jar>("fatJar") {
	manifest {
		attributes("Main-Class" to "fintech.todolist.TodolistApplication")
	}
	archiveClassifier.set("fat")

	from(sourceSets.main.get().output)

	dependsOn(configurations.runtimeClasspath)
	from({
		configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
	})
}

tasks.bootBuildImage {
	builder.set("paketobuildpacks/builder-jammy-base:latest")
}
