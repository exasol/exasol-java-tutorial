package com.exasol.javatutorial.markdown;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MarkdownStatisticsScannerTest {
    private static Stream<Arguments> generateSampleText() {
        return Stream.of( //
                Arguments.of("This text has **five** words.", 5, 0, 1),
                Arguments.of("# Überschrift\n\nUnd noch ein bisschen Fließtext.", 6, 1, 1),
                Arguments.of("# Heading\n\nParagraph one\n\nParagraph two", 5, 1, 2));
    }

    @MethodSource("generateSampleText")
    @ParameterizedTest
    void testCountElements(final String markdown, final int expectedWords, final int expectedHeadlines,
            final int expectedParagraphs) {
        final MarkdownStatisticsScanner scanner = new MarkdownStatisticsScanner();
        final TextStatistics statistics = scanner.scan(markdown);
        assertAll( //
                () -> assertThat(statistics.getWords(), equalTo(expectedWords)),
                () -> assertThat(statistics.getHeadings(), equalTo(expectedHeadlines)),
                () -> assertThat(statistics.getParagraphs(), equalTo(expectedParagraphs)));
    }
}