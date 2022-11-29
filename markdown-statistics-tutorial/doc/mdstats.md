# Markdown Statistics

## Before you Start

If you haven't done so already, we recommend that you do the ["Hello world"](../../hello-world-tutorial/doc/hello_world.md) tutorial first. We are building on that knowledge here.

## What you are Going to Learn in This Tutorial

1. Creating a scalar script that emits multiple columns.
1. Creating an integration test that exercises the scalar script.
1. Using [`exasol-testcontainers`](https://github.com/exasol/exasol-testcontainers) to install and control an Exasol instance automatically.
1. Using [`test-db-builder-java`](https://github.com/exasol/test-db-builder-java) to prepare the data for the tests.
1. Using [`hamcrest-resultset-matcher`](https://github.com/exasol/hamcrest-resultset-matcher) to compare expectations with the actual results.

## The Scenario

Imagine the following scenario:

You work as a data scientist at a big online tech-magazine. The people in your company publish about a hundred news articles and how-tos on your blog per year.
To separate content from layout the articles are written in Markdown and rendered via Cascading Stylesheets (CSS).

Each article that gets read has a counter to find out which ones are popular.

Your chief editor mentions the suspicion that there is a distinct sweet spot for how long popular articles should be and how they are structured.

As the old saying goes: "Without data, all you have is an opinion." So you take on the task to verify or debunk your editor's gut feeling.

The missing piece in the puzzle is that you need statistics from the blog articles.

## Prerequisites

As in the ["Hello World"](../../hello-world-tutorial/doc/hello_world.md) example we first need a running Exasol instance again.

```bash
docker run --name exasoldb --publish 127.0.0.1:8563:8563 --publish 127.0.0.1:2580:2580 --detach --privileged --stop-timeout 120  exasol/docker-db:7.1.10
```

Note that after terminating the instance, the data will be gone.

Another prerequisite is a web client for managing files in Bucket FS. If you haven't had contact with BucketFS, please check out the official [BucketFS documentation](https://docs.exasol.com/administration/on-premise/bucketfs/bucketfs.htm). BucketFS is not exactly trivial, so you should definitely take a few minutes to familiarize yourself with the concepts and usage.

If you are unsure which client to pick, use [curl](https://curl.se/).

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
1. [`test-db-builder-java`](https://github.com/exasol/test-db-builder-java): quickly create test database structures and contents
1. [`hamcrest-resultset-matcher`](https://github.com/exasol/hamcrest-resultset-matcher): check the results of JDBC queries against expectations

## Implementing the Markdown Statistics Function

One goal of this tutorial is to demonstrate how to deal with dependencies to libraries in Java UDFs. It is obvious from the task description that we need to parse Markdown. There is no point in reinventing the wheel here, so we need an existing Markdown parser library.

We picked [`commonmark-java`](https://github.com/commonmark/commonmark-java) for this purpose, a lean library under active development that does the job without any transitive dependencies. Perfect for our tutorial.

To count the Markdown headings, paragraphs and words, we need to implement our own visitor that we inject into the Markdown parser. If you haven't heard about the [visitor pattern](https://en.wikipedia.org/wiki/Visitor_pattern) yet, take a little time to read up on the topic. In a nutshell it is a tree walker that can be configured with callback different implementations depending on the element of the tree that is is processing. Since most parsers generate trees, the visitor is a common way to deal with the parsing results.

Take a moment look at the source of the class [`MarkdownStatitsitcsScanner`](../../src/main/java/com/exasol/javatutorial/markdown/MarkdownStatisticsScanner.java].

You will understand the main principle if you look at this snippet:

```java
import org.commonmark.node.*;

public class ElementCountVisitor extends AbstractVisitor {
    private int headingCount = 0;
    // ...
    
    @Override
    public void visit(final Heading heading) {
        ++this.headingCount;
        super.visit(heading);
    }
    
    // ...
    
    public TextStatistics getTextStatistics() {
        return new TextStatistics(this.wordCount, this.headingCount, this.paragraphCount);
    }
}
```

The first thing you'll notice is the `org.commonmark` import. This is where our external dependency comes into play.

What you see here is a call-back function that the tree walker calls every time it hits a heading. Counting is then simple enough, we just keep a counter in an instance variable. Needless to say that this class is intended to be disposed after each scan. Since we are doing only a single scan, nothing bad can happen.

Counting paragraphs is equally simple. There is a little bit more work involved for counting the words. We won't go into details here, since that is besides the point of this tutorial. You can always read the code if you are curious.

The next piece of the puzzle is abstracting the statistics scan, so that the parser and visitor details don't shine through in the calls from the main entry point.

```java
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;

public class MarkdownStatisticsScanner {
    private final Parser parser = Parser.builder().build();

    public TextStatistics scan(final String markdown) {
        final Node document = this.parser.parse(markdown);
        final ElementCountVisitor elementCounter = new ElementCountVisitor();
        document.accept(elementCounter);
        return elementCounter.getTextStatistics();
    }
}
```

Here we are first creating a parser. We do this as an instance variable, so that if `scan` was called multiple times, we would save the effort of doing that repeatedly.

Inside the `scan` method, we let the Markdown parser parse the Markdown document, then we inject our own visitor into the tree walker of the document and fire it up using `accept`.

As a result our visitor now has all the statistics and we can return them.

Finally we write the entry point of the scalar function.

```java
import com.exasol.ExaIterator;
import com.exasol.ExaMetadata;

public class MdStat {
    private static final String SCRIPT_PARAMETER_1_NAME = "MDTEXT";
    private static final MarkdownStatisticsScanner SCANNER = new MarkdownStatisticsScanner();
    
    // ...

    public static void run(final ExaMetadata metadata, final ExaIterator context) throws Exception {
        final String markdownText = context.getString(SCRIPT_PARAMETER_1_NAME);
        final TextStatistics statistics = SCANNER.scan(markdownText);
        context.emit(statistics.getWords(), statistics.getHeadings(), statistics.getParagraphs());
    }
}
```

As you probably expected this consists of getting the input value from the script parameter `MDTEXT`, running the Markdown statistics scanner we just created and emitting the results.

That's it where the implementation is concerned. Not too complicated, is it?

## Packaging the Fat JAR

Now that we have all the parts of the implementation in place, we still need to deploy our extension. Unlike in our "hello world" example inlining the Java code into SQL code is not an option anymore. First we have an external dependency that is packaged as a JAR and thus impossible to inline, second the attempt to inline even this mildly complex Java code would get unmaintainable as SQL / Java hybrid code in a single script. Third and by no means last, demonstrating how to deploy and test a properly packaged Java plug-in is the whole point of this tutorial.

So now, let's check our options. We could either package our own code into a JAR file and then register that in the SQL code next to the registration of the external dependency.

This is perfectly possible and there are situations where this absolutely makes sense, e.g. if the license of the external dependency for example requires that you distribute it in unmodified form. Or if you want your users to be able to update your part and the external dependency independently, e.g. if the library is a driver. In that case updating the driver might be necessary in order to keep your plugin compatible with newer versions of the service the driver attaches to. We take that approach in our [Virtual Schemas](https://github.com/exasol/virtual-schemas).

On the other hand, it is more convenient for users if they need to install only a single artifact as a plugin. Fewer moving parts means fewer places where things can go wrong. In this case you should build an all-in-one JAR or as the folk saying goes a "fat JAR".

The good news is that you don't have to do a lot here. Basically it boils down to telling your build framework to make it happen.

Please take a look a the file `[pom.xml](../../pom.xml)` again. In the `plugins` section there is one entry using the `maven-assembly-plugin` that tells Maven to build everything into a single JAR archive &mdash; including all dependencies.

If you are not using Maven, you will easily find instructions how to build fat JARs with your tool.

```xml
<plugin>
    <artifactId>maven-assembly-plugin</artifactId>
    <groupId>org.apache.maven.plugins</groupId>
    <version>3.3.0</version>
    <configuration>
        <descriptors>
            <descriptor>src/assembly/all-dependencies.xml</descriptor>
        </descriptors>
        <finalName>exasol-java-tutorial</finalName>
        <appendAssemblyId>false</appendAssemblyId>
    </configuration>
    <executions>
        <execution>
            <id>make-assembly</id>
            <phase>package</phase>
            <goals>
                <goal>single</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

You can copy and paste that part into your own projects. The only parts you might want to modify are the plug-in version number and the `finalName` parameter.

When it comes to naming, note that in this example we sacrificed an important aspect of artifact naming for the sake of simplicity. The artifact name does not contain a version number, which for a production plug-in it **absolutely should feature**! We did this so that the documentation and the test code remain stable across versions of this tutorial. In a real-world scenario we highly recommend that you include the version number in the JAR name! You can use the Maven variable `project.version` to achieve this.

```xml
<finalName>exasol-java-tutorial-${project.version}</finalName>
```

And one more word of warning: if you build distribution files, you take over responsibility for everything they contain. So make sure that you read and respect the licenses of what you are including, check regularly for security updates and be prepared to maintain or replace the external dependencies should the original maintainers abandon them.

Okay, now that we got this out of the way, let's recap what the fruits of our Maven build step are. We now have a JAR file called `markdown-statistics-tutorial.jar` that contains everything we need to install it as a standalone plug-in.

## Building an Integration Test

What do we want to achieve?

We want to have an integration test that without any interactions

1. sets up an Exasol instance,
1. installs the scalar script we wrote,
1. creates test data,
1. calls the scalar script on the test data and
1. verifies the results.

Please take a look at [`MarkdownStatisticsScalarScriptIT`](../src/test/java/com/exasol/javatutorial/markdown/MarkdownStatisticsScalarScriptIT.java). In this JUnit5-based test we use the [`exasol-testcontainer`](https://github.com/exasol/exasol-testcontainers) to install, start and control an Exasol instance agains which we then run the tests.

First we create an instance of an `ExasolTestcontainer` which is an extension of the original testcontainers that provides all the necessary Exasol specifics. For more clarity you also find the necessary imports in the snippet below.

```java
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import com.exasol.containers.ExasolContainer;
// ...

    @Container
    private static final ExasolContainer<? extends ExasolContainer<?>> EXASOL = new ExasolContainer<>().withReuse(true);
```

The `.withReuse(true)` call keeps the container running across multiple tests, thus greatly reducing the overall test time.

Be careful with clean test setup though when using this option. While the Exasol testcontainer cleans up the database content with the start of each new test class, it does not do this for each individual test case. If you want to avoid problems, either make sure your tests create resources that don't interfere with each other or purge the database in `beforeEach()`.

Next we set up the [`test-db-builder-java`](https://github.com/exasol/test-db-builder-java). This allows us to create database structures and populate them with data in a compact and readable fashion.

```java
import com.exasol.dbbuilder.dialects.DatabaseObjectFactory;
import com.exasol.dbbuilder.dialects.exasol.ExasolObjectFactory;
// ...
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

The first import gives us an enum value that we will later use to switch off type matching in a test where the Java type is irrelevant because we are only interested in the value.
The second import restores the `assertThat()` method which was dropped from the central JUnit libraries with JUnit5.
Finally, we have the import of the actual result set matcher.

Okay, let's now put it all together and create a test case.

```java
    @Test
    void testGetMarkdownStatistics() {
        final Schema schema = factory.createSchema("SCHEMA_FOR_MARKDOWN_STATISICS");
        schema.createTable("TEXTS", "TEXT", "VARCHAR(2000)") //
                .insert("# A Headline\n\nAnd some text with _emphasis_.");
        installMdStatsScript();
        
        final ResultSet result = execute("SELECT MDSTAT(TEXT) FROM TEXTS");
        
        final int expectedWords = 7, expectedHeadings = 1, expectedParagraphs = 1;
        assertThat(result,
                table().row(expectedWords, expectedHeadings, expectedParagraphs).matches(NO_JAVA_TYPE_CHECK));
    }
```

As with every good test case, we have three distinct phases here: setup, execution and result verification.

In the setup phase, we first create a database schema with a table that contains a markdown text. Then we install the Java scalar script.

After that is done, we execute a query with the scalar script and record the result.

Finally we hold the result against our expectation which are seven words, one heading and one paragraph.

We tell the matcher to only match the values and ignore any Java type differences. Otherwise we would have to provide `long` values as expectations, which makes the tests less readable.

The method `table()` is a factory method in the `com.exasol.matcher.ResultSetStructureMatcher`, that creates a table matcher builder. This sounds more complicated than it is. Resultsets can be quite complex, that's why we use a builder pattern to define the expectations.

The `row(7, 1, 1)` call adds an expected data row and `matches(NO_JAVA_TYPE_CHECK)` builds the matcher. As mentioned previously we only care about the result values, hence he switch of Java type checking.

As you can see, the test is intentionally kept compact. Six lines is all it takes to formulate the test.

The most complicated part is abstracted by the method `installMdStatsScript()`:

First it uploads the JAR archive containing the scalar script from the project's `target/markdown-statistics-tutorial.jar` path to the default bucket in BucketFS under `/markdown-statistics-tutorial.jar`.

```java
import com.exasol.bucketfs.Bucket;
import com.exasol.bucketfs.BucketAccessException;
// ...
    private static final String JAR_FILE_NAME = "markdown-statistics-tutorial.jar";
    // ...
    
    private void installMdStatsScript() {
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
    private void installMdStatsScript() {
    ...
        final String createScalarScriptSql = "CREATE JAVA SCALAR SCRIPT MDSTAT(MDTEXT VARCHAR(2000000))"
                + " EMITS (WORDS INTEGER, HEADINGS INTEGER, PARAGRAPHS INTEGER) AS\n" //
                + "    %scriptclass " + MdStat.class.getName() + ";\n" //
                + "    %jar /buckets/" + bucket.getFullyQualifiedBucketName() + "/" + JAR_FILE_NAME + ";\n" //
                + "/";
        execute(createScalarScriptSql);
    }
```

Note here that we inject the class name, the path in the bucket and the file name in the SQL, to avoid coupling.

## Running the Tests

Use the following [Maven](https://maven.apache.org/) command to build the JAR archive and run all tests:

```sql
mvn verify
```

While building and unit testing the extension are fast, especially the first integration test will take a while, since the testcontainers first need to download the Exasol Docker image and then start the dockerized instance. Expect around five to ten minutes depending on your Internet connection and the power of your development machine.

## Deploying an Extension as JAR

While we now have everything fully automated, you are probably curious on how you would deploy this by hand in a production environment.

If you looked closely at the code of the `installMdStatsScript()` earlier, you probably already guess the answer.

First you need a web client to upload the JAR file to a bucket in BucketFS. As mentioned earlier in this tutorial, please read the official [BucketFS documentation](https://docs.exasol.com/administration/on-premise/bucketfs/bucketfs.htm) before you proceed.

We used a `docker-db` image of Exasol, where a BucketFS service and a default bucket are available right from the start, so you can skip the setup process.

**Important:** The _write_-password of that bucket is _auto-generated_. If you want to write to that bucket, you must read the generated password from the file `/exa/etc/EXAConf` inside the docker environment. It is Base64 encoded, so you must decode it first. In your integration tests the `exasol-testcontainer` does that automatically for you, but if you want to do it by hand, you don't have that luxury.

Given an unmodified `docker-db` image, you can however extract the generated password like this into the variable `WPASSWD`:

```bash
export CONTAINERID = <id-if-exasol-docker-container>
export WPASSWD=$(docker exec -it "$CONTAINERID" sed -nr 's/ *WritePasswd = ([0-9a-zA-Z]*=?=?)/\1/p' /exa/etc/EXAConf | base64 -di)
```

Now use your web client to upload the JAR file to the default bucket under the following path `localhost:2850/bfsdefault/default/markdown-statistics-tutorial.jar` with user "`w`" and the password you just extracted.

Here is an example with `curl`:

```bash
curl -X PUT -T target/markdown-statistics-tutorial.jar "http://w:$WPASSWD@localhost:2580/default/exasol-java-turorial.jar"
```

Finally, run `CREATE JAVA SCALAR SCRIPT` to register the Function.

```sql
--/
CREATE JAVA SCALAR SCRIPT JAVA_TUTORIAL.MDSTAT(MDTEXT VARCHAR(2000000))
EMITS (WORDS INTEGER, HEADINGS INTEGER, PARAGRAPHS INTEGER) AS
    %scriptclass com.exasol.javatutorial.markdown.MdStats;
    %jar /buckets/bfsdefault/default/markdown-statistics-tutorial.jar;
/
```

## Running "MDSTAT"

To run the scalar script you can embed the function into a `SELECT` statement:

```
SELECT MDSTAT('This is a **Markdown** example')
```

Of course if you want it to be more realistic, you should create a table that contains a couple of Markdown texts and then use that as an input for the function instead of a `VARCHAR` constant.

## Summary

In this tutorial you created a scalar script that took an input value and emitted three columns. Then you built a standalone JAR file from your code and an external dependency, which serves as UDF plug-in.

You let the test framework start a docker-based Exasol instance in a test container for you. You registered the plug-in as a scalar script, set up the necessary database structure and contents for the test with the test database builder.

Then you executed a query that contains the scalar script.

Finally you used a dedicated Hamcrest matcher to verify that the results from the query matched your expectations.