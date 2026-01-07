plugins {
    id("java")
    // Версии здесь можно не указывать, если они указаны в корневом build.gradle.kts
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

dependencies {
    // Библиотеки gRPC (без Spring!)
    implementation("io.grpc:grpc-protobuf:1.60.0")
    implementation("io.grpc:grpc-stub:1.60.0")

    // Нужно для Java 21+, чтобы сгенерированный код не ругался на отсутствие аннотаций
    compileOnly("javax.annotation:javax.annotation-api:1.3.2")
}

tasks.test {
    useJUnitPlatform()
}