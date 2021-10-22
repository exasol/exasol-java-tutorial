-- Start with a clean slate:
DROP SCHEMA IF EXISTS JAVA_TUTORIAL CASCADE;
--

CREATE SCHEMA JAVA_TUTORIAL;

--/
CREATE JAVA SCALAR SCRIPT JAVA_TUTORIAL.HELLO() RETURNS VARCHAR(20) AS
    class HELLO {
        static String run(ExaMetadata metadata, ExaIterator context) throws Exception {
            return "Hello world!";
        }
    }
/;

SELECT HELLO();