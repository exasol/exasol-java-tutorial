package com.exasol.javatutorial.tls;

/**
 * Class that holds common constants relevant in the TLS context, such as the names of the properties that control which
 * truststore is used.
 */
public final class Constants {
    public static final String TRUSTSTORE_PATH_PROPERTY = "javax.net.ssl.trustStore";
    public static final String TRUSTSTORE_PASSWORD_PROPERTY = "javax.net.ssl.trustStorePassword";
    public static final String JAVA_HOME_PROPERTY = "java.home";
    @SuppressWarnings("java:S2068") // while this is a hard-coded password, it is the one that is public. It is the
    // default password of key stores shipped with Java.
    public static final String DEFAULT_TRUSTSTORE_PASSWORD = "changeit";

    private Constants() {
        // prevent instantiation
    }
}
