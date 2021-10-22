package com.exasol.javatutorial.markdown;

import static org.hamcrest.MatcherAssert.assertThat;

import java.sql.*;

import org.junit.jupiter.api.*;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.exasol.containers.ExasolContainer;
import com.exasol.dbbuilder.dialects.DatabaseObjectFactory;
import com.exasol.dbbuilder.dialects.Schema;
import com.exasol.dbbuilder.dialects.exasol.ExasolObjectFactory;
import com.exasol.matcher.ResultSetStructureMatcher;

@Tag("slow")
@Testcontainers
class MarkdownStatisticsScalarScriptIT {
    @Container
    private static final ExasolContainer<? extends ExasolContainer<?>> EXASOL = new ExasolContainer<>();
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
        final Schema schema = MarkdownStatisticsScalarScriptIT.factory.createSchema("SCHEMA_FOR_MARKDOWN_STATISICS");
        schema.createTable("TEXTS", "TEXT", "VARCHAR(2000)") //
                .insert("# A Headline\n\nAnd some text with _emphasis_.");
        installMdStatsScript();
        final ResultSet result = execute("SELECT MDSTATS(TEXT) FROM TEXTS", "run MDSTATS query");
        assertThat(result, ResultSetStructureMatcher.table().row(7, 1, 1).matches());
    }

    private void installMdStatsScript() throws AssertionError {
        final String createScalarScriptSql = "CREATE JAVA SCALAR SCRIPT MDSTAT(TEXT VARCHAR(2000))"
                + " EMITS (WORDS INTEGER, HEADINGS INTEGER, PARAGRAPHS INTEGER) AS\n"
                + "    %scriptclass com.exasol.tutorial,markdown.MdStat\n"
                + "    %jar /bucketfs/default/default/exasol-java-tutorial-0.1.0.jar\n" + "/";
        execute(createScalarScriptSql, "create scalar script for test");
    }

    private ResultSet execute(final String sql, final String action) throws AssertionError {
        try (final Statement statement = connection.createStatement()) {
            statement.execute(sql);
            return statement.getResultSet();
        } catch (final SQLException exception) {
            throw new AssertionError("Unable to " + action + ".", exception);
        }
    }

}
