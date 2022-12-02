package com.exasol.javatutorial.tls.test;

import com.exasol.bucketfs.Bucket;
import com.exasol.bucketfs.BucketAccessException;
import com.exasol.containers.ExasolContainer;
import com.exasol.dbbuilder.dialects.exasol.ExasolObjectFactory;
import com.exasol.dbbuilder.dialects.exasol.ExasolSchema;
import com.exasol.javatutorial.tls.Certificates;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import static com.exasol.javatutorial.tls.test.TlsTestConstants.LETS_ENCRYPT_ROOT_CA_1;
import static java.util.Objects.requireNonNullElse;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;

@Testcontainers
class CertificatesIT {
    @Container
    final private static ExasolContainer<? extends ExasolContainer<?>> EXASOL;

    static {
        try (final ExasolContainer<? extends ExasolContainer<?>> container = new ExasolContainer<>()) {
            EXASOL = container.withReuse(true);
        }
    }

    private static final String JAR_FILE_NAME = "tls-tutorial.jar";
    private static Connection connection;
    private static ExasolObjectFactory factory;

    @BeforeAll
    static void beforeAll() throws SQLException {
        connection = EXASOL.createConnection();
        factory = new ExasolObjectFactory(EXASOL.createConnection());
    }

    @Test
    void testListCertificates() {
        final ExasolSchema schema = factory.createSchema("TLS_TUTORIAL_SCHEMA");
        final String fullyQualifiedScriptName = "\"" + schema.getName() + "\".\"CERTIFICATES\"";
        installCertificatesScript(fullyQualifiedScriptName);
        final List<List<String>> rows = execute("SELECT " + fullyQualifiedScriptName + "()");
        assertThat(rows, hasItem(List.of(LETS_ENCRYPT_ROOT_CA_1, "Internet Security Research Group", "", "US")));
    }

    private void installCertificatesScript(String fullyQualifiedScriptName) throws AssertionError {
        final Bucket bucket = EXASOL.getDefaultBucket();
        try {
            bucket.uploadFile(Path.of("target", JAR_FILE_NAME), JAR_FILE_NAME);
        } catch (FileNotFoundException | BucketAccessException exception) {
            throw new AssertionError("Unable to install scalar Script " + JAR_FILE_NAME, exception);
        } catch (final TimeoutException exception) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(exception);
        }
        final String createScalarScriptSql = "CREATE JAVA SCALAR SCRIPT " + fullyQualifiedScriptName + "()"
                + " EMITS (CN VARCHAR(2000), O VARCHAR(2000), OU VARCHAR(2000), C VARCHAR(2)) AS\n" //
                + "    %scriptclass " + Certificates.class.getName() + ";\n" //
                + "    %jar /buckets/" + bucket.getFullyQualifiedBucketName() + "/" + JAR_FILE_NAME + ";\n" //
                + "/";
        execute(createScalarScriptSql);
    }

    private List<List<String>> execute(final String sql) throws AssertionError {
        try (final Statement statement = connection.createStatement()) {
            if (statement.execute(sql)) {
                return convertResultsetToListOfRows(statement);
            } else {
                return null;
            }
        } catch (final SQLException exception) {
            throw new AssertionError("Unable to execute: " + sql, exception);
        }
    }

    private static List<List<String>> convertResultsetToListOfRows(Statement statement) throws SQLException {
        final List<List<String>> rows = new ArrayList<>();
        try (final ResultSet result = statement.getResultSet()) {
            while (result.next()) {
                rows.add(List.of(requireNonNullElse(result.getString(1), ""),
                        requireNonNullElse(result.getString(2), ""),
                        requireNonNullElse(result.getString(3), ""),
                        requireNonNullElse(result.getString(4), "")));
            }
            return rows;
        }
    }
}
