# exasol-java-tutorial 0.2.0, released 2023-09-05

Code name: TLS tutorial

## Summary

In this release we added a tutorial that explains how to use TLS in combination with Exasol. It especially focuses on the case where [User Defined Functions (UDFs) need TLS connections](../../tls-tutorial/doc/tls_in_udfs.md).

The tutorial also contains a general [introduction](../../tls-tutorial/doc/tls_introduction.md) to TLS to establish the base know-how for the tutorial.

And we added a part that teaches you [how to deal with TLS certificates](../../tls-tutorial/doc/use_your_own_certificate.md).

## Features

* #7: Added TLS UDF Tutorial
* #11: Added general information about TLS with Exasol
* #14: Added Tutorial for `IMPORT` with organization-issued TLS certificate

## Bugfixes

* #3: Upgraded dependencies to fix security issue.
* #5: Fixed issues reported by broken links checker

# Documentation

* #17: Removed duplicate mention of Docker installation.
* #20: Made clearer which link leads to `IMPORT` TLS example
* #21: Mentioned that the TLS tutorial is not tested on WSL
* #23: Improved explanation about signature calculation
* #28: Explained relationship between host authentication and TLS certificate in more detail

## Dependency Updates

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
* Added `com.exasol:project-keeper-maven-plugin:2.9.11`
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
* Added `com.exasol:project-keeper-maven-plugin:2.9.11`
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
