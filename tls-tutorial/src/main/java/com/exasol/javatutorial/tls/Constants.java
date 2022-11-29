package com.exasol.javatutorial.tls;

public final class Constants {
    public static final String TRUSTSTORE_PATH_PROPERTY = "javax.net.ssl.trustStore";
    public static final String TRUSTSTORE_PASSWORD_PROPERTY = "javax.net.ssl.trustStorePassword";
    public static final String JAVA_HOME_PROPERTY = "java.home";
    public static final String DEFAULT_TRUSTSTORE_PASSWORD = "changeit";

    private Constants() {
        // prevent instantiation
    }
}
