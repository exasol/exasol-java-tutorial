package com.exasol.javatutorial.tls;

import java.util.HashMap;
import java.util.Map;

/**
 * This class provides access to the parts of the LDAP path that makes up a certificate name.
 */
public class CertificateName {
    private final Map<String, String> namePathElements = new HashMap<>();

    /**
     * Create a certificate name object from a string.
     *
     * @param name name as LDAP path
     *
     * @return certificate name object
     */
    public static CertificateName of(final String name) {
        return new CertificateName(name);
    }

    private CertificateName(final String name) {
        final String[] pathElements = name.split("\\s{0,256},\\s{0,256}");
        for(String pathElement : pathElements) {
            if(pathElement != null) {
                final int assigmentOperatorPosition = pathElement.indexOf("=");
                if(assigmentOperatorPosition >= 1) {
                    final String key = pathElement.substring(0, assigmentOperatorPosition);
                    final String value = pathElement.substring(assigmentOperatorPosition + 1);
                    namePathElements.put(key, value);
                }
            }
        }
    }

    /**
     * Get the common name of the certificate.
     * <p>
     * The common name is what most people would perceive as the certificate name. It is not guaranteed to be unique
     * like the distinguished name though.
     * </p>
     *
     * @return common name
     */
    public String getCommonName() {
        return this.namePathElements.get("CN");
    }

    /**
     * Get the issuing organization.
     *
     * @return name of the issuing organization
     */
    public String getOrganization() {
        return this.namePathElements.get("O");
    }

    /**
     * Get the organizational unit of the issuer.
     *
     * @return organizational unit
     */
    public String getOrganizationalUnit() {
        return this.namePathElements.get("OU");
    }

    /**
     * Get the country of the issuer.
     *
     * @return 2-letter country code
     */
    public String getCountry() {
        return this.namePathElements.get("C");
    }
}
