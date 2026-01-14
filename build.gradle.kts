import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    java
    id("org.springframework.boot") version "3.5.7" apply false
    id("io.spring.dependency-management") version "1.1.7" apply false
    id("com.diffplug.spotless") version "6.25.0" apply false
}

allprojects {
    group = "dev.vundirov"
    version = "1.0.0"

    repositories {
        mavenCentral()
    }
}
var totalTests = 0
var totalPassed = 0
var totalFailed = 0
var totalSkipped = 0
subprojects {
    apply(plugin = "java")
    apply(plugin = "com.diffplug.spotless")
    apply(plugin = "io.spring.dependency-management")

    configure<JavaPluginExtension> {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(21))
        }
    }

    configure<com.diffplug.gradle.spotless.SpotlessExtension> {
        java {
            googleJavaFormat("1.17.0")
            removeUnusedImports()
            trimTrailingWhitespace()
            endWithNewline()
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()

        testLogging {
            // Опции для стандартного вывода (LIFECYCLE)
            events(
                TestLogEvent.FAILED,
                TestLogEvent.PASSED,
                TestLogEvent.SKIPPED,
                TestLogEvent.STANDARD_OUT
            )
            exceptionFormat = TestExceptionFormat.FULL
            showExceptions = true
            showCauses = true
            showStackTraces = true

            // Настройки для уровней DEBUG и INFO
            debug {
                events(
                    TestLogEvent.STARTED,
                    TestLogEvent.FAILED,
                    TestLogEvent.PASSED,
                    TestLogEvent.SKIPPED,
                    TestLogEvent.STANDARD_ERROR,
                    TestLogEvent.STANDARD_OUT
                )
                exceptionFormat = TestExceptionFormat.FULL
            }
            info.events = debug.events
            info.exceptionFormat = debug.exceptionFormat
        }

        addTestListener(object : TestListener {
            override fun beforeSuite(suite: TestDescriptor) {}
            override fun beforeTest(testDescriptor: TestDescriptor) {}
            override fun afterTest(testDescriptor: TestDescriptor, result: TestResult) {}
            override fun afterSuite(suite: TestDescriptor, result: TestResult) {
                // Если это корневой набор тестов в модуле
                if (suite.parent == null) {
                    totalTests += result.testCount.toInt()
                    totalPassed += result.successfulTestCount.toInt()
                    totalFailed += result.failedTestCount.toInt()
                    totalSkipped += result.skippedTestCount.toInt()
                }

            }
        })
    }
}
gradle.buildFinished {
    if (totalTests > 0) {
        val summary = "FINAL PROJECT TEST SUMMARY"
        val results = "Total: $totalTests | Passed: $totalPassed | Failed: $totalFailed | Skipped: $totalSkipped"
        val border = "=".repeat(results.length + 4)

        println("\n$border")
        println("| ${summary.padEnd(results.length)} |")
        println("| $results |")
        println("$border\n")
    }
}

