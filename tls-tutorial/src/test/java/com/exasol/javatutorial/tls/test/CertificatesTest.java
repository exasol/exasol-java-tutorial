package com.exasol.javatutorial.tls.test;

import com.exasol.*;
import com.exasol.javatutorial.tls.Certificates;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.exasol.javatutorial.tls.test.TlsTestConstants.LETS_ENCRYPT_ROOT_CA_1;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(MockitoExtension.class)
class CertificatesTest {
    @Test
    void testListCertificates(@Mock final ExaMetadata metadataMock)
            throws Exception
    {
        final ExaIteratorStub contextStub = new ExaIteratorStub();
        Certificates.run(metadataMock, contextStub);
        final List<List<Object>> emittedRows = contextStub.getEmittedRows();
        assertAll(
                () -> assertThat(emittedRows, hasItem(hasItem(LETS_ENCRYPT_ROOT_CA_1))),
                () -> assertThat(emittedRows, everyItem(hasSize(6)))
        );
    }
}
