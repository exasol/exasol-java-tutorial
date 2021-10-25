package com.exasol.javatutorial.markdown;

import static com.exasol.matcher.ResultSetStructureMatcher.table;
import static com.exasol.matcher.TypeMatchMode.NO_JAVA_TYPE_CHECK;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.sql.*;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.*;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.exasol.bucketfs.Bucket;
import com.exasol.bucketfs.BucketAccessException;
import com.exasol.containers.ExasolContainer;
import com.exasol.dbbuilder.dialects.DatabaseObjectFactory;
import com.exasol.dbbuilder.dialects.Schema;
import com.exasol.dbbuilder.dialects.exasol.ExasolObjectFactory;

@Tag("slow")
@Testcontainers
class MarkdownStatisticsScalarScriptIT {
    private static final String JAR_FILE_NAME = "exasol-java-tutorial.jar";
    @Container
    private static final ExasolContainer<? extends ExasolContainer<?>> EXASOL = new ExasolContainer<>().withReuse(true);
    private static Connection connection;
    private static DatabaseObjectFactory factory;

    @BeforeAll
    static void beforeAll() throws SQLException {
        connection = EXASOL.createConnection();
        factory = new ExasolObjectFactory(connection);
    }

    @AfterAll
    static void afterAll() throws SQLException {
        if (MarkdownStatisticsScalarScriptIT.connection != null) {
            MarkdownStatisticsScalarScriptIT.connection.close();
        }
    }

    @Test
    void testGetMarkdownStatistics() {
        final Schema schema = factory.createSchema("SCHEMA_FOR_MARKDOWN_STATISICS");
        schema.createTable("TEXTS", "TEXT", "VARCHAR(2000)") //
                .insert("# A Headline\n\nAnd some text with _emphasis_.");
        installMdStatsScript();
        final ResultSet result = execute("SELECT MDSTAT(TEXT) FROM TEXTS");
        assertThat(result, table().row(7, 1, 1).matches(NO_JAVA_TYPE_CHECK));
    }

    private void installMdStatsScript() throws AssertionError {
        final Bucket bucket = EXASOL.getDefaultBucket();
        try {
            bucket.uploadFile(Path.of("target", JAR_FILE_NAME), JAR_FILE_NAME);
        } catch (FileNotFoundException | BucketAccessException exception) {
            throw new AssertionError("Unable to install scalar Script " + JAR_FILE_NAME, exception);
        } catch (final TimeoutException exception) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(exception);
        }
        final String createScalarScriptSql = "CREATE JAVA SCALAR SCRIPT MDSTAT(MDTEXT VARCHAR(2000))"
                + " EMITS (WORDS INTEGER, HEADINGS INTEGER, PARAGRAPHS INTEGER) AS\n" //
                + "    %scriptclass " + MdStat.class.getName() + ";\n" //
                + "    %jar /buckets/" + bucket.getFullyQualifiedBucketName() + "/" + JAR_FILE_NAME + ";\n" //
                + "/";
        execute(createScalarScriptSql);
    }

    private ResultSet execute(final String sql) throws AssertionError {
        try (final Statement statement = connection.createStatement()) {
            statement.execute(sql);
            return statement.getResultSet();
        } catch (final SQLException exception) {
            throw new AssertionError("Unable to execute: " + sql, exception);
        }
    }
}