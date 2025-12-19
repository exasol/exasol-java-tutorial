# Exasol Java Tutorial 0.3.3, released 2025-12-19

Code name: Fix CVE-2025-48924 in org.apache.commons:commons-lang3:jar:3.16.0:test

## Summary

This release fixes vulnerability CVE-2025-48924 in test dependency `org.apache.commons:commons-lang3:jar:3.16.0:test`.

## Security

* #59: Fixed CVE-2025-48924 in `org.apache.commons:commons-lang3:jar:3.16.0:test`

## Dependency Updates

### Exasol Java Tutorial

#### Compile Dependency Updates

* Updated `com.exasol:udf-api-java:1.0.4` to `1.0.8`

#### Test Dependency Updates

* Updated `com.exasol:exasol-testcontainers:7.1.6` to `7.2.2`
* Updated `com.exasol:hamcrest-resultset-matcher:1.6.4` to `1.7.2`
* Updated `com.exasol:test-db-builder-java:3.5.3` to `3.6.4`
* Updated `org.hamcrest:hamcrest:2.2` to `3.0`
* Added `org.junit.jupiter:junit-jupiter-api:6.0.1`
* Added `org.junit.jupiter:junit-jupiter-params:6.0.1`
* Removed `org.junit.jupiter:junit-jupiter:5.10.2`
* Updated `org.mockito:mockito-junit-jupiter:5.10.0` to `5.21.0`
* Removed `org.testcontainers:junit-jupiter:1.19.6`
* Added `org.testcontainers:testcontainers-junit-jupiter:2.0.3`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.0.3` to `2.0.5`
* Updated `com.exasol:project-keeper-maven-plugin:5.2.2` to `5.4.4`
* Updated `com.exasol:quality-summarizer-maven-plugin:0.2.0` to `0.2.1`
* Updated `io.github.git-commit-id:git-commit-id-maven-plugin:9.0.1` to `9.0.2`
* Updated `org.apache.maven.plugins:maven-artifact-plugin:3.6.0` to `3.6.1`
* Updated `org.apache.maven.plugins:maven-clean-plugin:3.4.1` to `3.5.0`
* Updated `org.apache.maven.plugins:maven-compiler-plugin:3.14.0` to `3.14.1`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.5.0` to `3.6.2`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.5.3` to `3.5.4`
* Updated `org.apache.maven.plugins:maven-resources-plugin:3.3.1` to `3.4.0`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.5.3` to `3.5.4`
* Updated `org.codehaus.mojo:flatten-maven-plugin:1.7.0` to `1.7.3`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.18.0` to `2.20.1`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.13` to `0.8.14`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:5.1.0.4751` to `5.5.0.6356`

### Markdown-statistics-tutorial

#### Compile Dependency Updates

* Updated `com.exasol:udf-api-java:1.0.4` to `1.0.8`
* Updated `org.commonmark:commonmark:0.21.0` to `0.27.0`
* Updated `org.slf4j:slf4j-jdk14:2.0.9` to `2.0.17`

#### Test Dependency Updates

* Updated `com.exasol:exasol-testcontainers:7.1.6` to `7.2.2`
* Updated `com.exasol:hamcrest-resultset-matcher:1.6.4` to `1.7.2`
* Updated `com.exasol:test-db-builder-java:3.5.3` to `3.6.4`
* Updated `org.hamcrest:hamcrest:2.2` to `3.0`
* Added `org.junit.jupiter:junit-jupiter-api:6.0.1`
* Added `org.junit.jupiter:junit-jupiter-params:6.0.1`
* Removed `org.junit.jupiter:junit-jupiter:5.10.2`
* Updated `org.mockito:mockito-junit-jupiter:5.10.0` to `5.21.0`
* Removed `org.testcontainers:junit-jupiter:1.19.6`
* Added `org.testcontainers:testcontainers-junit-jupiter:2.0.3`

#### Plugin Dependency Updates

* Updated `com.exasol:artifact-reference-checker-maven-plugin:0.4.3` to `0.4.4`
* Updated `com.exasol:error-code-crawler-maven-plugin:2.0.3` to `2.0.5`
* Updated `com.exasol:project-keeper-maven-plugin:5.2.2` to `5.4.4`
* Updated `com.exasol:quality-summarizer-maven-plugin:0.2.0` to `0.2.1`
* Updated `io.github.git-commit-id:git-commit-id-maven-plugin:9.0.1` to `9.0.2`
* Updated `org.apache.maven.plugins:maven-artifact-plugin:3.6.0` to `3.6.1`
* Updated `org.apache.maven.plugins:maven-assembly-plugin:3.7.1` to `3.8.0`
* Updated `org.apache.maven.plugins:maven-clean-plugin:3.4.1` to `3.5.0`
* Updated `org.apache.maven.plugins:maven-compiler-plugin:3.14.0` to `3.14.1`
* Added `org.apache.maven.plugins:maven-dependency-plugin:3.9.0`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.5.0` to `3.6.2`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.5.3` to `3.5.4`
* Updated `org.apache.maven.plugins:maven-jar-plugin:3.4.2` to `3.5.0`
* Updated `org.apache.maven.plugins:maven-resources-plugin:3.3.1` to `3.4.0`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.5.3` to `3.5.4`
* Updated `org.codehaus.mojo:flatten-maven-plugin:1.7.0` to `1.7.3`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.18.0` to `2.20.1`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.13` to `0.8.14`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:5.1.0.4751` to `5.5.0.6356`

### Tls-tutorial

#### Compile Dependency Updates

* Updated `com.exasol:udf-api-java:1.0.4` to `1.0.8`

#### Test Dependency Updates

* Updated `com.exasol:exasol-testcontainers:7.1.6` to `7.2.2`
* Updated `com.exasol:hamcrest-resultset-matcher:1.6.4` to `1.7.2`
* Updated `com.exasol:test-db-builder-java:3.5.3` to `3.6.4`
* Updated `org.hamcrest:hamcrest:2.2` to `3.0`
* Added `org.junit.jupiter:junit-jupiter-api:6.0.1`
* Added `org.junit.jupiter:junit-jupiter-params:6.0.1`
* Removed `org.junit.jupiter:junit-jupiter:5.10.2`
* Updated `org.mockito:mockito-junit-jupiter:5.10.0` to `5.21.0`
* Removed `org.testcontainers:junit-jupiter:1.19.6`
* Added `org.testcontainers:testcontainers-junit-jupiter:2.0.3`

#### Plugin Dependency Updates

* Updated `com.exasol:artifact-reference-checker-maven-plugin:0.4.3` to `0.4.4`
* Updated `com.exasol:error-code-crawler-maven-plugin:2.0.3` to `2.0.5`
* Updated `com.exasol:project-keeper-maven-plugin:5.2.2` to `5.4.4`
* Updated `com.exasol:quality-summarizer-maven-plugin:0.2.0` to `0.2.1`
* Updated `io.github.git-commit-id:git-commit-id-maven-plugin:9.0.1` to `9.0.2`
* Updated `org.apache.maven.plugins:maven-artifact-plugin:3.6.0` to `3.6.1`
* Updated `org.apache.maven.plugins:maven-assembly-plugin:3.7.1` to `3.8.0`
* Updated `org.apache.maven.plugins:maven-clean-plugin:3.4.1` to `3.5.0`
* Updated `org.apache.maven.plugins:maven-compiler-plugin:3.14.0` to `3.14.1`
* Added `org.apache.maven.plugins:maven-dependency-plugin:3.9.0`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.5.0` to `3.6.2`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.5.3` to `3.5.4`
* Updated `org.apache.maven.plugins:maven-jar-plugin:3.4.2` to `3.5.0`
* Updated `org.apache.maven.plugins:maven-resources-plugin:3.3.1` to `3.4.0`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.5.3` to `3.5.4`
* Updated `org.codehaus.mojo:flatten-maven-plugin:1.7.0` to `1.7.3`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.18.0` to `2.20.1`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.13` to `0.8.14`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:5.1.0.4751` to `5.5.0.6356`

### Testing-tutorial

#### Compile Dependency Updates

* Updated `com.exasol:udf-api-java:1.0.4` to `1.0.8`

#### Test Dependency Updates

* Updated `com.exasol:exasol-testcontainers:7.1.6` to `7.2.2`
* Updated `com.exasol:hamcrest-resultset-matcher:1.6.4` to `1.7.2`
* Updated `com.exasol:test-db-builder-java:3.5.3` to `3.6.4`
* Updated `org.hamcrest:hamcrest:2.2` to `3.0`
* Added `org.junit.jupiter:junit-jupiter-api:6.0.1`
* Added `org.junit.jupiter:junit-jupiter-params:6.0.1`
* Removed `org.junit.jupiter:junit-jupiter:5.10.2`
* Updated `org.mockito:mockito-junit-jupiter:5.10.0` to `5.21.0`
* Removed `org.testcontainers:junit-jupiter:1.19.6`
* Added `org.testcontainers:testcontainers-junit-jupiter:2.0.3`

#### Plugin Dependency Updates

* Updated `com.exasol:artifact-reference-checker-maven-plugin:0.4.3` to `0.4.4`
* Updated `com.exasol:error-code-crawler-maven-plugin:2.0.3` to `2.0.5`
* Updated `com.exasol:project-keeper-maven-plugin:5.2.2` to `5.4.4`
* Updated `com.exasol:quality-summarizer-maven-plugin:0.2.0` to `0.2.1`
* Updated `io.github.git-commit-id:git-commit-id-maven-plugin:9.0.1` to `9.0.2`
* Updated `org.apache.maven.plugins:maven-artifact-plugin:3.6.0` to `3.6.1`
* Updated `org.apache.maven.plugins:maven-assembly-plugin:3.7.1` to `3.8.0`
* Updated `org.apache.maven.plugins:maven-clean-plugin:3.4.1` to `3.5.0`
* Updated `org.apache.maven.plugins:maven-compiler-plugin:3.14.0` to `3.14.1`
* Added `org.apache.maven.plugins:maven-dependency-plugin:3.9.0`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.5.0` to `3.6.2`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.5.3` to `3.5.4`
* Updated `org.apache.maven.plugins:maven-jar-plugin:3.4.2` to `3.5.0`
* Updated `org.apache.maven.plugins:maven-resources-plugin:3.3.1` to `3.4.0`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.5.3` to `3.5.4`
* Updated `org.codehaus.mojo:flatten-maven-plugin:1.7.0` to `1.7.3`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.18.0` to `2.20.1`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.13` to `0.8.14`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:5.1.0.4751` to `5.5.0.6356`
