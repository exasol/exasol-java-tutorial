package com.exasol.javatutorial.markdown;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;

/**
 * This class extracts {@link TextStatistics} from Markdown text.
 */
public class MarkdownStatisticsScanner {
    private final Parser parser = Parser.builder().build();

    /**
     * Scan a markdown text to get statics
     *
     * @param markdown input text
     * @return text statistics
     */
    public TextStatistics scan(final String markdown) {
        final Node document = this.parser.parse(markdown);
        final ElementCountVisitor elementCounter = new ElementCountVisitor();
        document.accept(elementCounter);
        return elementCounter.getTextStatistics();
    }
}