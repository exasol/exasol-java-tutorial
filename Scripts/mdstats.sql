


CREATE JAVA SCALAR SCRIPT JAVA_TUTORIAL.MDSTAT(MDTEXT VARCHAR(2000))
EMITS (WORDS INTEGER, HEADINGS INTEGER, PARAGRAPHS INTEGER) AS
    %scriptclass com.exasol.javatutorial.markdown.MdStats;
    %jar /buckets/bfsdefault/default/exasol-java-tutorial.jar;
/