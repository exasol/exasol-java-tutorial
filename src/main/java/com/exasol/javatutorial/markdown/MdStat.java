package com.exasol.javatutorial.markdown;

import com.exasol.ExaIterator;
import com.exasol.ExaMetadata;

/**
 * Wrapper class for the entry point for the MDSTAT scalar script.
 */
public final class MdStat {
    private MdStat() {
        // prevent instantiation
    }

    /**
     * Entry point for the MDSTAT scalar script.
     *
     * @param metadata script metadata (unused)
     *
     * @param context  script context
     */
    @SuppressWarnings({ "java:S112", "java:S1172" })
    static void run(final ExaMetadata metadata, final ExaIterator context) throws Exception {
        final String mardownText = context.getString(0);
        final MarkdownStatisticsScanner scanner = new MarkdownStatisticsScanner();
        final TextStatistics statistics = scanner.scan(mardownText);
        context.emit(statistics.getWords(), statistics.getHeadings(), statistics.getParagraphs());
    }
}