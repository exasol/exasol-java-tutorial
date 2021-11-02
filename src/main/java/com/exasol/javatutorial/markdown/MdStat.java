package com.exasol.javatutorial.markdown;

import com.exasol.ExaIterator;
import com.exasol.ExaMetadata;

/**
 * Wrapper class for the entry point for the MDSTAT scalar script.
 */
public class MdStat {
    private static final String SCRIPT_PARAMETER_1_NAME = "MDTEXT";
    private static final MarkdownStatisticsScanner SCANNER = new MarkdownStatisticsScanner();

    private MdStat() {
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
        final String markdownText = context.getString(SCRIPT_PARAMETER_1_NAME);
        final TextStatistics statistics = SCANNER.scan(markdownText);
        context.emit(statistics.getWords(), statistics.getHeadings(), statistics.getParagraphs());
    }
}