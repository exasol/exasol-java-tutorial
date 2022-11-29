package com.exasol.javatutorial.tls;

import com.exasol.ExaIterator;
import com.exasol.ExaMetadata;

import java.security.cert.X509Certificate;
import java.util.List;

import static java.util.Objects.requireNonNullElse;

public class Certificates {
    private Certificates() {
        // prevent instantiation
    }

    /**
     * Entry point for the MDSTAT scalar script.
     *
     * @param metadata script metadata (unused)
     *
     * @param context  script context
     *
     * @throws Exception exception caught during script execution
     */
    // Unused parameters and throwing of class Exception required to match entry method signature.
    @SuppressWarnings({ "java:S112", "java:S1172" })
    public static void run(final ExaMetadata metadata, final ExaIterator context) throws Exception {
        final TrustStoreManager manager = new TrustStoreManager();
        final List<X509Certificate> certificates = manager.listCertificates();
        for(final X509Certificate certificate : certificates){
            final CertificateName name = CertificateName.of(certificate.getSubjectDN().getName());
            context.emit(requireNonNullElse(name.getCommonName(), ""),
                    requireNonNullElse(name.getOrganization(), ""),
                    requireNonNullElse(name.getOrganizationalUnit(), ""),
                    requireNonNullElse(name.getCountry(), ""));
        }
    }
}
