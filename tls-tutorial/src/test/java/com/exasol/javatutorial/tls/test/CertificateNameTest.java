package com.exasol.javatutorial.tls.test;

import com.exasol.javatutorial.tls.CertificateName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class CertificateNameTest {

    @Test
    void getDistinguishedName() {
        final CertificateName name = CertificateName.of("CN=ISRG Root X1, O=Internet Security Research Group, C=US");
        assertThat(name.getCommonName(), equalTo("ISRG Root X1"));
    }

    @Test
    void getOrganization() {
        final CertificateName name = CertificateName.of("CN=Amazon Root CA 4, O=Amazon, C=US");
        assertThat(name.getOrganization(), equalTo("Amazon"));
    }

    @Test
    void getOrganizationalUnit() {
        final CertificateName name =
                CertificateName.of("CN=DigiCert Global Root G2, OU=www.digicert.com, O=DigiCert Inc, C=US");
        assertThat(name.getOrganizationalUnit(), equalTo("www.digicert.com"));
    }

    @Test
    void getCountry() {
        final CertificateName name = CertificateName.of("CN=SwissSign Silver CA - G2, O=SwissSign AG, C=CH");
        assertThat(name.getCountry(), equalTo("CH"));
    }
}