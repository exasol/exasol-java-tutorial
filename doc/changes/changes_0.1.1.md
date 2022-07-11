# Exasol Java tutorial 0.1.1, released 2022-??-??

Code name: 0.1.1: Upgrade dependencies

## Features

* #3: Upgraded dependencies to fix [CVE-2022-21724](https://ossindex.sonatype.org/vulnerability/0f319d1b-e964-4471-bded-db3aeb3c3a29?component-type=maven&component-name=org.postgresql.postgresql&utm_source=ossindex-client&utm_medium=integration&utm_content=1.1.1) in the PostgreSQL JDBC driver.

## Bug fixes

* #5: Fixed issues reported by broken links checker

## Dependency Updates

### Compile Dependency Updates

* Updated `org.commonmark:commonmark:0.18.0` to `0.18.2`

### Test Dependency Updates

* Updated `com.exasol:exasol-testcontainers:5.1.1` to `6.1.1`
* Updated `com.exasol:test-db-builder-java:3.2.1` to `3.3.2`
* Updated `org.junit.jupiter:junit-jupiter-engine:5.7.2` to `5.8.2`
* Updated `org.junit.jupiter:junit-jupiter-params:5.7.2` to `5.8.2`
* Updated `org.mockito:mockito-junit-jupiter:4.0.0` to `4.5.1`
* Updated `org.testcontainers:junit-jupiter:1.16.2` to `1.17.1`

### Plugin Dependency Updates

* Updated `com.exasol:artifact-reference-checker-maven-plugin:0.3.1` to `0.4.1`
* Updated `com.exasol:error-code-crawler-maven-plugin:0.1.1` to `1.1.1`
* Updated `com.exasol:project-keeper-maven-plugin:1.3.1` to `2.4.6`
* Updated `io.github.zlika:reproducible-build-maven-plugin:0.13` to `0.15`
* Updated `org.apache.maven.plugins:maven-compiler-plugin:3.8.1` to `3.10.1`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.0.0-M3` to `3.0.0`
* Updated `org.apache.maven.plugins:maven-jar-plugin:3.2.0` to `3.2.2`
* Added `org.codehaus.mojo:flatten-maven-plugin:1.2.7`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.7` to `2.10.0`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.7` to `0.8.8`
* Added `org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.1.2184`
* Updated `org.sonatype.ossindex.maven:ossindex-maven-plugin:3.1.0` to `3.2.0`
