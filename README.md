# Exasol Java tutorial

[![Build Status](https://github.com/exasol/exasol-java-tutorial/actions/workflows/ci-build.yml/badge.svg)](https://github.com/exasol/exasol-java-tutorial/actions/workflows/ci-build.yml)

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Aexasol-java-tutorial&metric=alert_status)](https://sonarcloud.io/dashboard?id=com.exasol%3Aexasol-java-tutorial)

[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Aexasol-java-tutorial&metric=security_rating)](https://sonarcloud.io/dashboard?id=com.exasol%3Aexasol-java-tutorial)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Aexasol-java-tutorial&metric=reliability_rating)](https://sonarcloud.io/dashboard?id=com.exasol%3Aexasol-java-tutorial)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Aexasol-java-tutorial&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=com.exasol%3Aexasol-java-tutorial)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Aexasol-java-tutorial&metric=sqale_index)](https://sonarcloud.io/dashboard?id=com.exasol%3Aexasol-java-tutorial)

[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Aexasol-java-tutorial&metric=code_smells)](https://sonarcloud.io/dashboard?id=com.exasol%3Aexasol-java-tutorial)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Aexasol-java-tutorial&metric=coverage)](https://sonarcloud.io/dashboard?id=com.exasol%3Aexasol-java-tutorial)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Aexasol-java-tutorial&metric=duplicated_lines_density)](https://sonarcloud.io/dashboard?id=com.exasol%3Aexasol-java-tutorial)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Aexasol-java-tutorial&metric=ncloc)](https://sonarcloud.io/dashboard?id=com.exasol%3Aexasol-java-tutorial)

If you are a Java developer and you are thinking about extending Exasol with new functionality, this tutorial takes you on a tour around Exasol's interfaces and the libraries we provide.

Not only do you learn how to implement [User Defined Functions (UDF)](https://docs.exasol.com/database_concepts/udf_scripts.htm) in Java, but you also will see how to run automated integration tests.

Topics we are touching in the tutorials:

* Scalar Script UDF (a script with a single input)
* Java code inlined into SQL code
* Java UDFs provided as JAR archives
* Exasol Scripting API for Java
* How to get an Exasol instance you can test against in automated tests
* How to set up tests and prepare test data
* How to check the results from SQL queries in a compact and readable way

## Tutorials

Start your journey with the obligatory [hello world](doc/tutorial/hello_world.md) tutorial to learn the basics of defining an inline Java extension that can be called from SQL.

Up your game with a full blown automated integration test that exercises a more complex Java extension that gets [statistics from Markdown](doc/tutorial/mdstats.md) text stored in an Exasol table. Learn to package the installation in a JAR archive in the course of this tutorial.

## Additional Information

* [Changelog](doc/changes/changelog.md)
* [Dependencies](dependencies.md)