# Exasol Java Tutorial 0.3.5, released 2026-??-??

Code name: Fixed vulnerability CVE-2026-9563 in org.eclipse.parsson:parsson:jar:1.1.7:test

## Summary

This release fixes the following vulnerability:

### CVE-2026-9563 (CWE-400) in dependency `org.eclipse.parsson:parsson:jar:1.1.7:test`
In Eclipse Parsson published Maven Central artifacts before version 1.1.8, the JSON parser did not enforce a default maximum on the number of characters consumed while parsing a single JSON document. Applications that parse attacker- controlled JSON can be forced to consume excessive CPU and memory by processing very large documents, including large arrays, objects, strings, numbers, whitespace, or nested structures, resulting in a denial of service. Eclipse Parsson 1.1.8 introduces a configurable maximum parsing limit with a default limit of 15 million parser-consumed characters.
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-9563?component-type=maven&component-name=org.eclipse.parsson%2Fparsson&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-9563
* https://github.com/eclipse-ee4j/parsson/pull/169
* https://gitlab.eclipse.org/security/vulnerability-reports/-/work_items/444

## Security

* #63: Fixed vulnerability CVE-2026-9563 in dependency `org.eclipse.parsson:parsson:jar:1.1.7:test`

## Dependency Updates

### Exasol Java Tutorial

#### Compile Dependency Updates

* Updated `com.exasol:udf-api-java:1.0.9` to `1.0.10`

#### Test Dependency Updates

* Updated `com.exasol:test-db-builder-java:4.0.0` to `4.0.1`
* Updated `org.junit.jupiter:junit-jupiter-api:6.0.1` to `6.1.2`
* Updated `org.junit.jupiter:junit-jupiter-params:6.0.1` to `6.1.2`

#### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:5.6.2` to `5.7.3`

### Markdown-statistics-tutorial

#### Compile Dependency Updates

* Updated `com.exasol:udf-api-java:1.0.9` to `1.0.10`
* Updated `org.commonmark:commonmark:0.28.0` to `0.29.0`

#### Test Dependency Updates

* Updated `com.exasol:test-db-builder-java:4.0.0` to `4.0.1`
* Updated `org.junit.jupiter:junit-jupiter-api:6.0.1` to `6.1.2`
* Updated `org.junit.jupiter:junit-jupiter-params:6.0.1` to `6.1.2`

#### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:5.6.2` to `5.7.3`

### Tls-tutorial

#### Compile Dependency Updates

* Updated `com.exasol:udf-api-java:1.0.9` to `1.0.10`

#### Test Dependency Updates

* Updated `com.exasol:test-db-builder-java:4.0.0` to `4.0.1`
* Updated `org.junit.jupiter:junit-jupiter-api:6.0.1` to `6.1.2`
* Updated `org.junit.jupiter:junit-jupiter-params:6.0.1` to `6.1.2`

#### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:5.6.2` to `5.7.3`

### Testing-tutorial

#### Compile Dependency Updates

* Updated `com.exasol:udf-api-java:1.0.9` to `1.0.10`

#### Test Dependency Updates

* Updated `com.exasol:test-db-builder-java:4.0.0` to `4.0.1`
* Updated `org.junit.jupiter:junit-jupiter-api:6.0.1` to `6.1.2`
* Updated `org.junit.jupiter:junit-jupiter-params:6.0.1` to `6.1.2`

#### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:5.6.2` to `5.7.3`
