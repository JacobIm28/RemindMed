import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.2.3"
	id("io.spring.dependency-management") version "1.1.4"
	kotlin("jvm") version "1.9.22"
	kotlin("plugin.spring") version "1.9.22"
}

group = "com.backend"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.springframework.boot:spring-boot-starter-web")
	// Used to connect to our database
	implementation("org.postgresql:postgresql:42.3.1")
	testImplementation("org.springframework.boot:spring-boot-starter-test")

	// for calling REST APIs with Ktor
	implementation("io.ktor:ktor-client-core:2.3.8")
	implementation("io.ktor:ktor-client-android:2.3.8")

	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")

	// for deserializing strings to objects
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.2")

	// for parsing JSON
	implementation("com.google.code.gson:gson:2.8.9")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
