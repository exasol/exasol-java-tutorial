# Exasol Java Tutorial 0.3.0, released 2023-??-??

Code name: Testing Tutorial

## Summary

In this release we added a tutorial that covers testing from the general concepts over test-first approaches like TDD and BDD to coverage measuring.

## Features

* 42: Added a testing tutorial

## Dependency Updates

### Exasol Java Tutorial

#### Compile Dependency Updates

* Removed `com.exasol:exasol-script-api:6.1.7`
* Added `com.exasol:udf-api-java:1.0.2`
* Removed `org.commonmark:commonmark:0.18.0`

#### Test Dependency Updates

* Updated `com.exasol:exasol-testcontainers:5.1.1` to `6.6.1`
* Updated `com.exasol:hamcrest-resultset-matcher:1.5.1` to `1.6.0`
* Updated `com.exasol:test-db-builder-java:3.2.1` to `3.5.0`
* Removed `org.junit.jupiter:junit-jupiter-engine:5.7.2`
* Removed `org.junit.jupiter:junit-jupiter-params:5.7.2`
* Added `org.junit.jupiter:junit-jupiter:5.10.0`
* Updated `org.mockito:mockito-junit-jupiter:4.0.0` to `5.5.0`
* Updated `org.testcontainers:junit-jupiter:1.16.2` to `1.19.0`

#### Plugin Dependency Updates

* Removed `com.exasol:artifact-reference-checker-maven-plugin:0.3.1`
* Updated `com.exasol:error-code-crawler-maven-plugin:0.1.1` to `1.3.0`
* Updated `com.exasol:project-keeper-maven-plugin:1.3.1` to `2.9.12`
* Updated `io.github.zlika:reproducible-build-maven-plugin:0.13` to `0.16`
* Removed `org.apache.maven.plugins:maven-assembly-plugin:3.3.0`
* Updated `org.apache.maven.plugins:maven-compiler-plugin:3.8.1` to `3.11.0`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.0.0-M3` to `3.4.0`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.0.0-M3` to `3.1.2`
* Removed `org.apache.maven.plugins:maven-jar-plugin:3.2.0`
* Removed `org.apache.maven.plugins:maven-resources-plugin:2.6`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.0.0-M3` to `3.1.2`
* Added `org.basepom.maven:duplicate-finder-maven-plugin:2.0.1`
* Added `org.codehaus.mojo:flatten-maven-plugin:1.5.0`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.7` to `2.16.0`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.7` to `0.8.10`
* Added `org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.1.2184`
* Updated `org.sonatype.ossindex.maven:ossindex-maven-plugin:3.1.0` to `3.2.0`

### Markdown-statistics-tutorial

#### Compile Dependency Updates

* Added `com.exasol:udf-api-java:1.0.2`
* Added `org.commonmark:commonmark:0.21.0`

#### Test Dependency Updates

* Added `com.exasol:exasol-testcontainers:6.6.1`
* Added `com.exasol:hamcrest-resultset-matcher:1.6.0`
* Added `com.exasol:test-db-builder-java:3.5.0`
* Added `org.hamcrest:hamcrest:2.2`
* Added `org.junit.jupiter:junit-jupiter:5.10.0`
* Added `org.mockito:mockito-junit-jupiter:5.5.0`
* Added `org.testcontainers:junit-jupiter:1.19.0`

#### Plugin Dependency Updates

* Added `com.exasol:artifact-reference-checker-maven-plugin:0.4.2`
* Added `com.exasol:error-code-crawler-maven-plugin:1.3.0`
* Added `com.exasol:project-keeper-maven-plugin:2.9.12`
* Added `io.github.zlika:reproducible-build-maven-plugin:0.16`
* Added `org.apache.maven.plugins:maven-assembly-plugin:3.6.0`
* Added `org.apache.maven.plugins:maven-clean-plugin:2.5`
* Added `org.apache.maven.plugins:maven-compiler-plugin:3.11.0`
* Added `org.apache.maven.plugins:maven-deploy-plugin:2.7`
* Added `org.apache.maven.plugins:maven-enforcer-plugin:3.4.0`
* Added `org.apache.maven.plugins:maven-failsafe-plugin:3.1.2`
* Added `org.apache.maven.plugins:maven-install-plugin:2.4`
* Added `org.apache.maven.plugins:maven-jar-plugin:3.3.0`
* Added `org.apache.maven.plugins:maven-resources-plugin:2.6`
* Added `org.apache.maven.plugins:maven-site-plugin:3.3`
* Added `org.apache.maven.plugins:maven-surefire-plugin:3.1.2`
* Added `org.basepom.maven:duplicate-finder-maven-plugin:2.0.1`
* Added `org.codehaus.mojo:flatten-maven-plugin:1.5.0`
* Added `org.codehaus.mojo:versions-maven-plugin:2.16.0`
* Added `org.jacoco:jacoco-maven-plugin:0.8.10`
* Added `org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.1.2184`
* Added `org.sonatype.ossindex.maven:ossindex-maven-plugin:3.2.0`

### Tls-tutorial

#### Compile Dependency Updates

* Added `com.exasol:udf-api-java:1.0.2`

#### Test Dependency Updates

* Added `com.exasol:exasol-testcontainers:6.6.1`
* Added `com.exasol:hamcrest-resultset-matcher:1.6.0`
* Added `com.exasol:test-db-builder-java:3.5.0`
* Added `org.hamcrest:hamcrest:2.2`
* Added `org.junit.jupiter:junit-jupiter:5.10.0`
* Added `org.mockito:mockito-junit-jupiter:5.5.0`
* Added `org.testcontainers:junit-jupiter:1.19.0`

#### Plugin Dependency Updates

* Added `com.exasol:artifact-reference-checker-maven-plugin:0.4.2`
* Added `com.exasol:error-code-crawler-maven-plugin:1.3.0`
* Added `com.exasol:project-keeper-maven-plugin:2.9.12`
* Added `io.github.zlika:reproducible-build-maven-plugin:0.16`
* Added `org.apache.maven.plugins:maven-assembly-plugin:3.6.0`
* Added `org.apache.maven.plugins:maven-clean-plugin:2.5`
* Added `org.apache.maven.plugins:maven-compiler-plugin:3.11.0`
* Added `org.apache.maven.plugins:maven-deploy-plugin:2.7`
* Added `org.apache.maven.plugins:maven-enforcer-plugin:3.4.0`
* Added `org.apache.maven.plugins:maven-failsafe-plugin:3.1.2`
* Added `org.apache.maven.plugins:maven-install-plugin:2.4`
* Added `org.apache.maven.plugins:maven-jar-plugin:3.3.0`
* Added `org.apache.maven.plugins:maven-resources-plugin:2.6`
* Added `org.apache.maven.plugins:maven-site-plugin:3.3`
* Added `org.apache.maven.plugins:maven-surefire-plugin:3.1.2`
* Added `org.basepom.maven:duplicate-finder-maven-plugin:2.0.1`
* Added `org.codehaus.mojo:flatten-maven-plugin:1.5.0`
* Added `org.codehaus.mojo:versions-maven-plugin:2.16.0`
* Added `org.jacoco:jacoco-maven-plugin:0.8.10`
* Added `org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.1.2184`
* Added `org.sonatype.ossindex.maven:ossindex-maven-plugin:3.2.0`

### Testing-tutorial

#### Compile Dependency Updates

* Added `com.exasol:udf-api-java:1.0.2`

#### Test Dependency Updates

* Added `com.exasol:exasol-testcontainers:6.6.1`
* Added `com.exasol:hamcrest-resultset-matcher:1.6.0`
* Added `com.exasol:test-db-builder-java:3.5.0`
* Added `org.hamcrest:hamcrest:2.2`
* Added `org.junit.jupiter:junit-jupiter:5.10.0`
* Added `org.mockito:mockito-junit-jupiter:5.5.0`
* Added `org.testcontainers:junit-jupiter:1.19.0`

#### Plugin Dependency Updates

* Added `com.exasol:artifact-reference-checker-maven-plugin:0.4.2`
* Added `com.exasol:error-code-crawler-maven-plugin:1.3.0`
* Added `com.exasol:project-keeper-maven-plugin:2.9.12`
* Added `io.github.zlika:reproducible-build-maven-plugin:0.16`
* Added `org.apache.maven.plugins:maven-assembly-plugin:3.6.0`
* Added `org.apache.maven.plugins:maven-clean-plugin:2.5`
* Added `org.apache.maven.plugins:maven-compiler-plugin:3.11.0`
* Added `org.apache.maven.plugins:maven-deploy-plugin:2.7`
* Added `org.apache.maven.plugins:maven-enforcer-plugin:3.4.0`
* Added `org.apache.maven.plugins:maven-failsafe-plugin:3.1.2`
* Added `org.apache.maven.plugins:maven-install-plugin:2.4`
* Added `org.apache.maven.plugins:maven-jar-plugin:3.3.0`
* Added `org.apache.maven.plugins:maven-resources-plugin:2.6`
* Added `org.apache.maven.plugins:maven-site-plugin:3.3`
* Added `org.apache.maven.plugins:maven-surefire-plugin:3.1.2`
* Added `org.basepom.maven:duplicate-finder-maven-plugin:2.0.1`
* Added `org.codehaus.mojo:flatten-maven-plugin:1.5.0`
* Added `org.codehaus.mojo:versions-maven-plugin:2.16.0`
* Added `org.jacoco:jacoco-maven-plugin:0.8.10`
* Added `org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.1.2184`
* Added `org.sonatype.ossindex.maven:ossindex-maven-plugin:3.2.0`
