# Exasol Java tutorial 0.1.0, released 2021-10-29

Code name: Scalar scripts

In version 0.1.0 of the `exasol-java-tutorial` we provided two tutorials. One is the obligatory "hello world" example that focuses on the absolute minimum code required to implement a Java User Defined Function (UDF).

The other one builds on the first and introduces a scalar script that emits three columns. The main focus of that tutorial is not on the implementation though but on how to create an automated integration test that verifies the UDF you wrote.

## Features

* #1: Added two tutorial for scalar scripts.

## Dependency Updates

### Compile Dependency Updates

* Added `com.exasol:exasol-script-api:6.1.7`
* Added `org.commonmark:commonmark:0.18.0`

### Test Dependency Updates

* Added `com.exasol:exasol-testcontainers:5.1.1`
* Added `com.exasol:hamcrest-resultset-matcher:1.5.1`
* Added `com.exasol:test-db-builder-java:3.2.1`
* Added `org.hamcrest:hamcrest:2.2`
* Added `org.junit.jupiter:junit-jupiter-engine:5.7.2`
* Added `org.junit.jupiter:junit-jupiter-params:5.7.2`
* Added `org.mockito:mockito-junit-jupiter:4.0.0`
* Added `org.testcontainers:junit-jupiter:1.16.2`

### Plugin Dependency Updates

* Added `com.exasol:artifact-reference-checker-maven-plugin:0.3.1`
* Added `com.exasol:error-code-crawler-maven-plugin:0.1.1`
* Added `com.exasol:project-keeper-maven-plugin:1.3.1`
* Added `io.github.zlika:reproducible-build-maven-plugin:0.13`
* Added `org.apache.maven.plugins:maven-assembly-plugin:3.3.0`
* Added `org.apache.maven.plugins:maven-clean-plugin:2.5`
* Added `org.apache.maven.plugins:maven-compiler-plugin:3.8.1`
* Added `org.apache.maven.plugins:maven-deploy-plugin:2.7`
* Added `org.apache.maven.plugins:maven-enforcer-plugin:3.0.0-M3`
* Added `org.apache.maven.plugins:maven-failsafe-plugin:3.0.0-M3`
* Added `org.apache.maven.plugins:maven-install-plugin:2.4`
* Added `org.apache.maven.plugins:maven-jar-plugin:3.2.0`
* Added `org.apache.maven.plugins:maven-resources-plugin:2.6`
* Added `org.apache.maven.plugins:maven-site-plugin:3.3`
* Added `org.apache.maven.plugins:maven-surefire-plugin:3.0.0-M3`
* Added `org.codehaus.mojo:versions-maven-plugin:2.7`
* Added `org.jacoco:jacoco-maven-plugin:0.8.7`
* Added `org.sonatype.ossindex.maven:ossindex-maven-plugin:3.1.0`
