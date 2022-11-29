package com.exasol.javatutorial.markdown;

/**
 * Parameter class for text statistics.
 */
public class TextStatistics {
    private final int words;
    private final int headings;
    private final int paragraphs;

    /**
     * Create a new instance of the {@link TextStatistics}.
     *
     * @param words      number of words in the text
     * @param headings   number of headings
     * @param paragraphs number of paragraphs
     */
    public TextStatistics(final int words, final int headings, final int paragraphs) {
        this.words = words;
        this.headings = headings;
        this.paragraphs = paragraphs;
    }

    /**
     * Get the number of words in the text.
     *
     * @return number of words
     */
    public int getWords() {
        return this.words;
    }

    /**
     * Get the number of headings in the text.
     *
     * @return number of headings
     */
    public int getHeadings() {
        return this.headings;
    }

    /**
     * Get the number of paragraphs in the text.
     *
     * @return number of paragraphs
     */
    public int getParagraphs() {
        return this.paragraphs;
    }
}