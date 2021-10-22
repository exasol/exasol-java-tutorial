package com.exasol.javatutorial.markdown;

import static java.lang.Character.charCount;
import static java.lang.Character.isLetter;

import org.commonmark.node.*;

/**
 * Visitor implementation that extracts text statistics from the syntax tree of a Markdown document.
 */
public class ElementCountVisitor extends AbstractVisitor {
    private int wordCount = 0;
    private int headingCount = 0;
    private int paragraph;

    @Override
    public void visit(final Heading heading) {
        ++this.headingCount;
        super.visit(heading);
    }

    @Override
    public void visit(final Paragraph paragraph) {
        ++this.paragraph;
        super.visit(paragraph);
    }

    @Override
    public void visit(final Text text) {
        final String literal = text.getLiteral();
        this.wordCount += countWordsInLiteral(literal);
        super.visit(text);
    }

    private int countWordsInLiteral(final String literal) {
        int words = 0;
        final int length = literal.length();
        boolean inWord = false;
        for (int offset = 0, uft8Char = 0; offset < length; //
                uft8Char = literal.codePointAt(offset), offset += charCount(uft8Char)) {
            if (isLetter(uft8Char)) {
                if (!inWord) {
                    ++words;
                    inWord = true;
                }
            } else {
                inWord = false;
            }
        }
        return words;
    }

    /**
     * Get the text statistics.
     *
     * @return text statistics containing the counts for different text elements
     */
    public TextStatistics getTextStatistics() {
        return new TextStatistics(this.wordCount, this.headingCount, this.paragraph);
    }
}