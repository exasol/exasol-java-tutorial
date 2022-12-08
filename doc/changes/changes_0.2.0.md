# exasol-java-tutorial 0.2.0, released 2022-12-??

Code name: TLS tutorial

## Summary

In this release we added a tutorial that explains how to use TLS in combination with Exasol. It especially focuses on the case where [User Defined Functions (UDFs) need TLS connections](../../tls-tutorial/doc/tls_in_udfs.md).

The tutorial also contains a general [introduction](../../tls-tutorial/doc/tls_introduction.md) to TLS to establish the base know-how for the tutorial.

## Features

* #7: Added TLS UDF Tutorial
* #11: Added general information about TLS with Exasol

## Bugfixes

* #3: Upgraded dependencies to fix security issue.
* #5: Fixed issues reported by broken links checker

## Dependency Updates

### Markdown-statistics-tutorial

#### Compile Dependency Updates

* Added `com.exasol:udf-api-java:1.0.1`
* Added `org.commonmark:commonmark:0.21.0`

#### Test Dependency Updates

* Added `com.exasol:exasol-testcontainers:6.4.0`
* Added `com.exasol:hamcrest-resultset-matcher:1.5.2`
* Added `com.exasol:test-db-builder-java:3.4.1`
* Added `org.hamcrest:hamcrest:2.2`
* Added `org.junit.jupiter:junit-jupiter-engine:5.9.1`
* Added `org.junit.jupiter:junit-jupiter-params:5.9.1`
* Added `org.mockito:mockito-junit-jupiter:4.9.0`
* Added `org.testcontainers:junit-jupiter:1.17.6`

#### Plugin Dependency Updates

* Added `com.exasol:artifact-reference-checker-maven-plugin:0.4.2`
* Added `com.exasol:error-code-crawler-maven-plugin:1.2.1`
* Added `com.exasol:project-keeper-maven-plugin:2.9.1`
* Added `io.github.zlika:reproducible-build-maven-plugin:0.16`
* Added `org.apache.maven.plugins:maven-assembly-plugin:3.4.2`
* Added `org.apache.maven.plugins:maven-clean-plugin:2.5`
* Added `org.apache.maven.plugins:maven-compiler-plugin:3.10.1`
* Added `org.apache.maven.plugins:maven-deploy-plugin:2.7`
* Added `org.apache.maven.plugins:maven-enforcer-plugin:3.1.0`
* Added `org.apache.maven.plugins:maven-failsafe-plugin:3.0.0-M7`
* Added `org.apache.maven.plugins:maven-install-plugin:2.4`
* Added `org.apache.maven.plugins:maven-jar-plugin:3.3.0`
* Added `org.apache.maven.plugins:maven-resources-plugin:2.6`
* Added `org.apache.maven.plugins:maven-site-plugin:3.3`
* Added `org.apache.maven.plugins:maven-surefire-plugin:3.0.0-M7`
* Added `org.codehaus.mojo:flatten-maven-plugin:1.3.0`
* Added `org.codehaus.mojo:versions-maven-plugin:2.13.0`
* Added `org.jacoco:jacoco-maven-plugin:0.8.8`
* Added `org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.1.2184`
* Added `org.sonatype.ossindex.maven:ossindex-maven-plugin:3.2.0`

### Tls-tutorial

#### Compile Dependency Updates

* Added `com.exasol:udf-api-java:1.0.1`

#### Test Dependency Updates

* Added `com.exasol:exasol-testcontainers:6.4.0`
* Added `com.exasol:hamcrest-resultset-matcher:1.5.2`
* Added `com.exasol:test-db-builder-java:3.4.1`
* Added `org.hamcrest:hamcrest:2.2`
* Added `org.junit.jupiter:junit-jupiter-engine:5.9.1`
* Added `org.junit.jupiter:junit-jupiter-params:5.9.1`
* Added `org.mockito:mockito-junit-jupiter:4.9.0`
* Added `org.testcontainers:junit-jupiter:1.17.6`

#### Plugin Dependency Updates

* Added `com.exasol:artifact-reference-checker-maven-plugin:0.4.2`
* Added `com.exasol:error-code-crawler-maven-plugin:1.2.1`
* Added `com.exasol:project-keeper-maven-plugin:2.9.1`
* Added `io.github.zlika:reproducible-build-maven-plugin:0.16`
* Added `org.apache.maven.plugins:maven-assembly-plugin:3.4.2`
* Added `org.apache.maven.plugins:maven-clean-plugin:2.5`
* Added `org.apache.maven.plugins:maven-compiler-plugin:3.10.1`
* Added `org.apache.maven.plugins:maven-deploy-plugin:2.7`
* Added `org.apache.maven.plugins:maven-enforcer-plugin:3.1.0`
* Added `org.apache.maven.plugins:maven-failsafe-plugin:3.0.0-M7`
* Added `org.apache.maven.plugins:maven-install-plugin:2.4`
* Added `org.apache.maven.plugins:maven-jar-plugin:3.3.0`
* Added `org.apache.maven.plugins:maven-resources-plugin:2.6`
* Added `org.apache.maven.plugins:maven-site-plugin:3.3`
* Added `org.apache.maven.plugins:maven-surefire-plugin:3.0.0-M7`
* Added `org.codehaus.mojo:flatten-maven-plugin:1.3.0`
* Added `org.codehaus.mojo:versions-maven-plugin:2.13.0`
* Added `org.jacoco:jacoco-maven-plugin:0.8.8`
* Added `org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.1.2184`
* Added `org.sonatype.ossindex.maven:ossindex-maven-plugin:3.2.0`
