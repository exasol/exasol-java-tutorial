package com.exasol.javatutorial.tls;

import com.exasol.ExaIterator;
import com.exasol.ExaMetadata;

import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.List;

import static java.util.Objects.requireNonNullElse;

/**
 * This class serves as the entry point for a User Defined Functions (short UDF) that lists the certificates available
 * in the truststore the UDF sees.
 * <p>
 * The actual entry point is a static method that has a predefined method signature, so that the Exasol UDF framework
 * finds it.
 * </p>
 * <p>
 * Refer to {@link Certificates#run(ExaMetadata, ExaIterator)} for details.
 * </p>
 */
public class Certificates {
    private Certificates() {
        // prevent instantiation
    }

    /**
     * Entry point for the CERTIFICATES scalar script.
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
        for (final X509Certificate certificate : certificates) {
            final CertificateName name = CertificateName.of(certificate.getSubjectX500Principal().getName());
            final Date validAfter = certificate.getNotBefore();
            final Date validBefore = certificate.getNotAfter();
            context.emit( //
                    allowEmpty(name.getCommonName()), //
                    allowEmpty(name.getOrganization()), //
                    allowEmpty(name.getOrganizationalUnit()), //
                    allowEmpty(name.getCountry()), //
                    validAfter.toString(), //
                    validBefore.toString() //
            );
        }
    }

    public static String allowEmpty(final String input) {
        return requireNonNullElse(input, "");
    }
}
