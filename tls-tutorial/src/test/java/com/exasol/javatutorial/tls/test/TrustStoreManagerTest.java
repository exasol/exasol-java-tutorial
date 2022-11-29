package com.exasol.javatutorial.tls.test;

import com.exasol.javatutorial.tls.TrustStoreManager;
import org.junit.jupiter.api.Test;

import java.security.cert.X509Certificate;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;

class TrustStoreManagerTest {
    private static String getDistinguishedName(X509Certificate certificate) {
        return certificate.getSubjectDN().getName();
    }

    @Test
    void testFindWellKnownCACertificateInList() {
        final TrustStoreManager manager = new TrustStoreManager();
        final List<X509Certificate> certificates = manager.listCertificates();
        final List<String> certificateDistinguishedNames = certificates.stream() //
                .map(TrustStoreManagerTest::getDistinguishedName) //
                .collect(Collectors.toList());
        assertThat(certificateDistinguishedNames, hasItems(containsString(TlsTestConstants.LETS_ENCRYPT_ROOT_CA_1)));
    }
}
