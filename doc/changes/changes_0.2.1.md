# Exasol Java Tutorial 0.2.1, released 2023-??-??

Code name: Dependency Upgrade

## Summary

This release fixes vulnerability CVE-2023-42503 in transitive test dependency to `org.apache.commons:commons-compress` via `exasol-testcontainers` by updating dependencies.

## Security

* #43: Fixed vulnerability CVE-2023-42503 in test dependency `org.apache.commons:commons-compress`

## Dependency Updates

### Markdown-statistics-tutorial

#### Compile Dependency Updates

* Updated `com.exasol:udf-api-java:1.0.2` to `1.0.3`

#### Test Dependency Updates

* Updated `com.exasol:exasol-testcontainers:6.6.1` to `6.6.2`
* Updated `com.exasol:hamcrest-resultset-matcher:1.6.0` to `1.6.1`
* Updated `com.exasol:test-db-builder-java:3.5.0` to `3.5.1`
* Updated `org.mockito:mockito-junit-jupiter:5.5.0` to `5.6.0`
* Updated `org.testcontainers:junit-jupiter:1.19.0` to `1.19.1`

#### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:2.9.11` to `2.9.13`

### Tls-tutorial

#### Compile Dependency Updates

* Updated `com.exasol:udf-api-java:1.0.2` to `1.0.3`

#### Test Dependency Updates

* Updated `com.exasol:exasol-testcontainers:6.6.1` to `6.6.2`
* Updated `com.exasol:hamcrest-resultset-matcher:1.6.0` to `1.6.1`
* Updated `com.exasol:test-db-builder-java:3.5.0` to `3.5.1`
* Updated `org.mockito:mockito-junit-jupiter:5.5.0` to `5.6.0`
* Updated `org.testcontainers:junit-jupiter:1.19.0` to `1.19.1`

#### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:2.9.11` to `2.9.13`
