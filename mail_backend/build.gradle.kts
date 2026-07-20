plugins {
	kotlin("jvm") version "2.2.21"
	kotlin("plugin.spring") version "2.2.21"
	id("org.springframework.boot") version "4.0.7"
	id("io.spring.dependency-management") version "1.1.7"
	kotlin("plugin.jpa") version "2.2.21"
	application
}

apply(plugin = "idea")
apply(plugin = "java")

application {
	mainClass = "de.thm.mni.mail_backend.MailBackendApplicationKt"
	applicationName = "mail_backend"
}

group = "de.thm.mni"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(24)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-webmvc")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("tools.jackson.module:jackson-module-kotlin")
	testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	//Database
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	runtimeOnly("com.h2database:h2")

	//Hashing
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.bouncycastle:bcprov-jdk18on:1.84")
	// JWT
	implementation("com.auth0:java-jwt:4.4.0")

	//OpenAPI
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:3.0.3")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.22.0")

	//Validation
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation(kotlin("stdlib"))

}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
