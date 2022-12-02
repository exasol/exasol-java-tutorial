package com.exasol.javatutorial.tls;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.PKIXParameters;
import java.security.cert.TrustAnchor;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class provides an abstraction on accessing a Java truststore.
 * <p>
 * In the context of the TLS tutorial it also serves as a demonstration on how to get
 * <a href="https://www.rfc-editor.org/rfc/rfc5280">X.509 certificates</a> from that truststore.
 * </p>
 */
public class TrustStoreManager {
    /**
     * Create a new instance of the {@link TrustStoreManager}.
     */
    public TrustStoreManager() {
        // Added for JavaDoc completeness
    }

    /**
     * Get the X.509 certificates that are contained in the truststore.
     *
     * @return list of certificates the truststore contains
     */
    public List<X509Certificate> listCertificates() {
        try {
            final KeyStore keyStore = loadKeyStore();
            final PKIXParameters parameters = new PKIXParameters(keyStore);
            final Set<TrustAnchor> trustAnchors = parameters.getTrustAnchors();
            return trustAnchors.stream().map(TrustAnchor::getTrustedCert).collect(Collectors.toList());
        } catch (KeyStoreException | InvalidAlgorithmParameterException exception) {
            throw new IllegalStateException("Unable to list certificates.", exception);
        }
    }

    private KeyStore loadKeyStore() {
        try {
            final String caCertsPath = System.getProperty(Constants.TRUSTSTORE_PATH_PROPERTY,
                    getDefaultTruststorePath().toString());
            final FileInputStream stream = new FileInputStream(caCertsPath);
            final KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            keystore.load(stream, getTruststorePassword().toCharArray());
            return keystore;
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException exception) {
            throw new IllegalStateException("Unable to load truststore.", exception);
        }
    }

    private Path getDefaultTruststorePath() {
        final String javaHome = System.getProperty(Constants.JAVA_HOME_PROPERTY);
        return Path.of(javaHome, "lib", "security", "cacerts");
    }

    private String getTruststorePassword() {
        return System.getProperty(Constants.TRUSTSTORE_PASSWORD_PROPERTY, Constants.DEFAULT_TRUSTSTORE_PASSWORD);
    }
}
