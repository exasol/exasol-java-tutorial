# Markdown Statistics

## What you are Going to Learn in This Tutorial

1. Creating a scalar script that emits multiple columns.
1. Creating an integration test that exercises the scalar script.
1. Using [`exasol-testcontainers`](https://github.com/exasol/exasol-testcontainers) to install and control an Exasol instance automatically.
1. Using [`test-db-builder-java`](https://github.com/exasol/test-db-builder-java) to prepare the data for the tests.
1. Using [`hamcrest-resultset-matcher`](https://github.com/exasol/hamcrest-resultset-matcher) to compare expectations with the actual results.

## The Scenario

Imagine the following scenario:

You work as a data scientist at a big online tech-magazine. The people in your company about a hundred news articles and how-tos on your blog.
To separate content from layout the articles are written in Markdown and rendered via Cascading Stylesheets (CSS).

Each article that gets read has a counter to find out which ones are popular.

Your chief editor mentions the suspicion that there is a distinct sweet spot for how long popular articles should be and how they are structured.

As the old saying goes: "Without data, all you have is an opinion." So you take on the task to verify or debunk your editor's gut feeling.

The missing piece in the puzzle is that you need statistics from the blog articles.

## Prerequisites

As in the ["Hello World"](hello_world.md) example we first need a running Exasol instance again.

```bash
docker run --name exasoldb -p 127.0.0.1:8563:8563 --detach --privileged --stop-timeout 120  exasol/docker-db:7.1.2
```

### Dependencies

This time we have a couple of dependencies that we need for our tutorial. In this section we are introducing the important ones.

If you look into the [Maven POM file](../../pom.xml) of this project you will notice that there are quite a couple more, most of them &mdash; especially the Maven plugins &mdash; serve quality assurance, the example would work without them just fine.

### Runtime Dependencies

Runtime dependencies are those libraries that end up in the final JAR file the contains the implementation of our Markdown statistics script. As you can see, we kept it to a bare minimum.

1. [`commonmark-java`](https://github.com/commonmark/commonmark-java): lean Markdown parser
1. [`exasol-script-api`](https://docs.exasol.com/database_concepts/udf_scripts/java.htm#UsingtheMavenrepository): API library for Exasol UDF scripts in Java

### Test Dependencies

Since this tutorial is also intended to teach you how to efficiently test your Exasol extensions, we will introduce a couple of dependencies that are only used to run the unit and integration tests.

1. [`exasol-testcontainer`](https://github.com/exasol/exasol-testcontainers): wrapper for Exasol's docker-db that automatically creates Exasol instances for your integration tests
1. [`hamcrest-resultset-matcher`](https://github.com/exasol/hamcrest-resultset-matcher): check the results of JDBC queries against expectations
1. [`test-db-builder-java`](https://github.com/exasol/test-db-builder-java): quickly create test database structures and contents

## Building an Integration Test

What do we want to achieve?

We want to have an integration test that without any interactions

1. sets up an Exasol instance,
1. installs the scalar script we wrote,
1. creates test data,
1. calls the scalar script on the test data and
1. verifies the results.

Please take a look at [`MarkdownStatisticsScalarScriptIT`](../../src/test/com/exasol/javatutorial/markdown/MarkdownStatisticsScalarScriptIT.java). In this JUnit5-based test we use the [`exasol-testcontainer`](https://github.com/exasol/exasol-testcontainers) to install, start and control an Exasol instance agains which we then run the tests.

First we create an instance of an `ExasolTestcontainer` which is an extension of the original testcontainers that provides all the necessary Exasol specifics. For more clarity you also find the necessary imports in the snippet below.

```java
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import com.exasol.containers.ExasolContainer;
...

    @Container
    private static final ExasolContainer<? extends ExasolContainer<?>> EXASOL = new ExasolContainer<>().withReuse(true);
```

The `.withReuse(true)` call keeps the container running across multiple tests, thus greatly reducing the overall test time.

Be careful with clean test setup though when using this option. While the Exasol testcontainer cleans up the database with the start of each new test class, it does not do this for each individual test case. If you want to avoid problems, either make sure your tests create resources that don't interfere with each other or purge the database in `beforeEach()`.

Next we set up the [`test-db-builder-java`](https://github.com/exasol/test-db-builder-java). This allows us to create database structures an populate them with data in a compact and readable fashion.

```java
import com.exasol.dbbuilder.dialects.DatabaseObjectFactory;
import com.exasol.dbbuilder.dialects.exasol.ExasolObjectFactory;
...
    private static Connection connection;
    private static DatabaseObjectFactory factory;

    @BeforeAll
    static void beforeAll() throws SQLException {
        connection = EXASOL.createConnection();
        factory = new ExasolObjectFactory(connection);
    }
```

As you can see, the `ExasolContainer` provides us with a database connection and we use that to initialize a `DatabaseObjectFactory`, the central starting point of all work with the `test-db-builder-java`.

Last but not least, we also import the [`hamcrest-resultset-matcher`](https://github.com/exasol/hamcrest-resultset-matcher), that let's us compare the JDBC result sets our test runs yield with our expectations.
If you haven't heard of [hamcrest matchers](http://hamcrest.org/), then now is the perfect time to take a break from this tutorial here and [read up on this very useful matcher framework](http://hamcrest.org/JavaHamcrest/tutorial).

```java
import static com.exasol.matcher.TypeMatchMode.NO_JAVA_TYPE_CHECK;
import static org.hamcrest.MatcherAssert.assertThat;
import com.exasol.matcher.ResultSetStructureMatcher;
```

The first import gives us an enum value that we will later use to switch of type matching in a test where the Java type is irrelevant because we are only interested in the value.
The second import restores the `assertThat()` method which was dropped from the central JUnit libraries with JUnit5.
Finally, we have the import of the actual result set matcher.

Okay, let's now put it all together and create a test case.

```java
@Test
void testGetMarkdownStatistics() {
    final Schema schema = factory.createSchema("SCHEMA_FOR_MARKDOWN_STATISICS");
    schema.createTable("TEXTS", "TEXT", "VARCHAR(2000)")
            .insert("# A Headline\n\nAnd some text with _emphasis_.");
    installMdStatsScript();

    final ResultSet result = execute("SELECT MDSTAT(TEXT) FROM TEXTS");

    assertThat(result, table().row(7, 1, 1).matches(NO_JAVA_TYPE_CHECK));
}
```

As with every good test case, we have three distinct phases here: setup, execution and result verification.

In the setup phase, we first create a database schema with a table that contains a markdown text. Then we install the Java scalar script.

After that is done, we execute a query with the scalar script and record the result.

Finally we hold the result against our expectation with are seven words, one heading and one paragraph.

We tell the matcher to only match the values and ignore any Java type differences. Otherwise we would have to provide `long` values as expectations, which makes the tests less readable.

The method `table()` is a factory method in the `com.exasol.matcher.ResultSetStructureMatcher`, that creates a table matcher builder. This sounds more complicated than it is. Resultsets can be quite complex, that's why we use a builder pattern to define the expectations.

The `row(7, 1, 1)` call adds an expected data row and `matches(NO_JAVA_TYPE_CHECK)` builds the matcher. As mentioned previously we only care about the result values, hence he switch of Java type checking.

As you can see, the test is intentionally kept compact. Six lines is all it takes to formulate the test.

The most complicated part is abstracted by the method `installMdStatsScript()`.

First it uploads the JAR archive containing the scalar script from the project's `target/exasol-java-tutorial.jar` path to the default bucket in BucketFS under `/exasol-java-tutorial.jar`.

```java
import com.exasol.bucketfs.Bucket;
import com.exasol.bucketfs.BucketAccessException;
...
    private static final String JAR_FILE_NAME = "exasol-java-tutorial.jar";
    ...
    
    private void installMdStatsScript() throws AssertionError {
        final Bucket bucket = EXASOL.getDefaultBucket();
        try {
            bucket.uploadFile(Path.of("target", JAR_FILE_NAME), JAR_FILE_NAME);
        } catch (...) {
            ...
        }
        ...
    }
```

Uploading a file has a lot of potential exceptions. While you could just let them fall through in a test, we caught them in the method and dealt with them to keep the method signatures of the test cases clean. If you are interested in the details, please check the source code.

After the upload is done, we register the scalar script:

```java
    private void installMdStatsScript() throws AssertionError {
    ...
        final String createScalarScriptSql = "CREATE JAVA SCALAR SCRIPT MDSTAT(MDTEXT VARCHAR(2000))"
                + " EMITS (WORDS INTEGER, HEADINGS INTEGER, PARAGRAPHS INTEGER) AS\n" //
                + "    %scriptclass " + MdStat.class.getName() + ";\n" //
                + "    %jar /buckets/" + bucket.getFullyQualifiedBucketName() + "/" + JAR_FILE_NAME + ";\n" //
                + "/";
        execute(createScalarScriptSql);
    }
```

Note here that we inject the class name, the path in the bucket and the file name in the SQL, to avoid coupling.

## Summary

In this tutorial you let the test framework start a docker-based Exasol instance in a test container for you. You installed a scalar script, set up the necessary database structure and contents for the test with the test database builder. Then you executed a query that contains the scalar script. Finally you used a dedicated Hamcrest matcher to verify that the results from the query matched your expectations.