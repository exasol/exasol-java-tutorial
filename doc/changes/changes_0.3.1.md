# Exasol Java Tutorial 0.3.1, released 2024-02-22

Code name: TLS Tutorial Improvements

## Summary

In this release we worked in review findings from @allipatev and @PeggySchmidtMittenzwei.

We also fixed CVE-2024-25710 by updating transitive dependencies.

> org.apache.commons:commons-compress:jar:1.24.0
> Loop with Unreachable Exit Condition ('Infinite Loop') vulnerability in Apache Commons Compress.This issue affects Apache Commons Compress: from 1.3 through 1.25.0.

## Features

* #18: Improved documentation of the Ubuntu VM installation for the TLS tutorial
* #41: Fixed TLS tutorial findings
* #51: Fixed CVE-2024-25710

## Dependency Updates

### Exasol Java Tutorial

#### Compile Dependency Updates

* Updated `com.exasol:udf-api-java:1.0.3` to `1.0.4`

#### Test Dependency Updates

* Updated `com.exasol:exasol-testcontainers:6.6.3` to `7.0.1`
* Updated `com.exasol:hamcrest-resultset-matcher:1.6.2` to `1.6.4`
* Updated `com.exasol:test-db-builder-java:3.5.1` to `3.5.3`
* Updated `org.junit.jupiter:junit-jupiter:5.10.1` to `5.10.2`
* Updated `org.mockito:mockito-junit-jupiter:5.7.0` to `5.10.0`
* Updated `org.testcontainers:junit-jupiter:1.19.2` to `1.19.6`

#### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:2.9.16` to `3.0.1`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.2.2` to `3.2.3`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.2.2` to `3.2.3`
* Added `org.apache.maven.plugins:maven-toolchains-plugin:3.1.0`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.16.1` to `2.16.2`

### Markdown-statistics-tutorial

#### Compile Dependency Updates

* Updated `com.exasol:udf-api-java:1.0.3` to `1.0.4`
* Updated `org.slf4j:slf4j-jdk14:2.0.5` to `2.0.9`

#### Test Dependency Updates

* Updated `com.exasol:exasol-testcontainers:6.6.3` to `7.0.1`
* Updated `com.exasol:hamcrest-resultset-matcher:1.6.2` to `1.6.4`
* Updated `com.exasol:test-db-builder-java:3.5.1` to `3.5.3`
* Updated `org.junit.jupiter:junit-jupiter:5.10.1` to `5.10.2`
* Updated `org.mockito:mockito-junit-jupiter:5.7.0` to `5.10.0`
* Updated `org.testcontainers:junit-jupiter:1.19.2` to `1.19.6`

#### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:2.9.16` to `3.0.1`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.2.2` to `3.2.3`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.2.2` to `3.2.3`
* Added `org.apache.maven.plugins:maven-toolchains-plugin:3.1.0`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.16.1` to `2.16.2`

### Tls-tutorial

#### Compile Dependency Updates

* Updated `com.exasol:udf-api-java:1.0.3` to `1.0.4`

#### Test Dependency Updates

* Updated `com.exasol:exasol-testcontainers:6.6.3` to `7.0.1`
* Updated `com.exasol:hamcrest-resultset-matcher:1.6.2` to `1.6.4`
* Updated `com.exasol:test-db-builder-java:3.5.1` to `3.5.3`
* Updated `org.junit.jupiter:junit-jupiter:5.10.1` to `5.10.2`
* Updated `org.mockito:mockito-junit-jupiter:5.7.0` to `5.10.0`
* Updated `org.testcontainers:junit-jupiter:1.19.2` to `1.19.6`

#### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:2.9.16` to `3.0.1`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.2.2` to `3.2.3`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.2.2` to `3.2.3`
* Added `org.apache.maven.plugins:maven-toolchains-plugin:3.1.0`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.16.1` to `2.16.2`

### Testing-tutorial

#### Compile Dependency Updates

* Updated `com.exasol:udf-api-java:1.0.3` to `1.0.4`

#### Test Dependency Updates

* Updated `com.exasol:exasol-testcontainers:6.6.3` to `7.0.1`
* Updated `com.exasol:hamcrest-resultset-matcher:1.6.2` to `1.6.4`
* Updated `com.exasol:test-db-builder-java:3.5.1` to `3.5.3`
* Updated `org.junit.jupiter:junit-jupiter:5.10.1` to `5.10.2`
* Updated `org.mockito:mockito-junit-jupiter:5.7.0` to `5.10.0`
* Updated `org.testcontainers:junit-jupiter:1.19.2` to `1.19.6`

#### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:2.9.16` to `3.0.1`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.2.2` to `3.2.3`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.2.2` to `3.2.3`
* Added `org.apache.maven.plugins:maven-toolchains-plugin:3.1.0`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.16.1` to `2.16.2`
