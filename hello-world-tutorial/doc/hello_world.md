# Hello world!

In this tutorial we are going to cover the absolute minimal amount of steps required to get a running "hello world" extension into Exasol.

## Prerequisites

You need a running Exasol instance and the simplest possible way to get there is to use our [`dockerdb`](https://github.com/exasol/docker-db) images and start Exasol in a Docker container.

```bash
docker run --name exasoldb --publish 127.0.0.1:8563:8563 --publish 127.0.0.1:2580:2580 --detach --privileged --stop-timeout 120  exasol/docker-db:7.1.10
```

Note that if you terminate the docker instance, the data inside is lost.

You can check the latest `docker-db` version in the [release list](https://github.com/exasol/docker-db/releases).

We forward two ports from the docker instance to the host here, the standard database port 8563 and the [BucketFS](https://docs.exasol.com/administration/on-premise/bucketfs/bucketfs.htm) port 2580. For this particular tutorial the BucketFS port is irrelevant because we are not using BucketFS, but it will come in handy in subsequent tutorials.

## Creating a Minimal Scalar Script

The absolute simplest possible extension that you can write in Java, is a [function script](https://docs.exasol.com/database_concepts/udf_scripts/java.htm).

"Function" here means a function that you can call as part of an SQL query. A "scalar function" is one with a single input value. Think of the function [`UPPER(string)`](https://docs.exasol.com/sql_references/functions/alphabeticallistfunctions/upper.htm) as an example. It has one input value and that is the string that should be converted to upper case.

You will need an SQL client to execute the commands on Exasol. You can either pick one from the [list of clients on our homepage](https://docs.exasol.com/connect_exasol/sql_clients.htm) or choose one of your liking that works with our JDBC or ODBC drivers.

Next you connect the client to the running docker instance. Since we forwarded the database port to the host computer, you can reach the database on `localhost` port `8563`.

By default, a the `SYS` user (i.e. the initial SQL administrator) has the password `exasol` on a fresh docker-db instance. We are talking about a disposable test instance here listening on your local machine. In any other scenario you should immediately change the password!

Check the user manual of your SQL client to find out how to connect to a database with username and password.

After you connected your SQL client, first create a schema where we can install the script.

```sql
CREATE SCHEMA JAVA_TUTORIAL;
```

Next define the script, its input and output and the implementation.

```sql
--/
CREATE JAVA SCALAR SCRIPT JAVA_TUTORIAL.HELLO() RETURNS VARCHAR(20) AS
    class HELLO {
        static String run(ExaMetadata metadata, ExaIterator context) throws Exception {
            return "Hello world!";
        }
    }
/;
```

For practice purposes I recommend typing the script, but you can also find a copy in [this tutorial's sources](../src/main/sql/hello.sql).

As you can tell from the function definition our function is called `HELLO`, lives in the schema `JAVA_TUTORIAL`, expects no input value and returns a variable length string with a maximum of 20 characters.

Since we are not defining the entry class name via a special directive, we need to make sure that the class name and the script name are identical. That is why the class is called `HELLO`, which breaks the established naming conventions in Java which says that class names should be upper camel case.

While you could call the scalar function `"Hello"` (including the double quotes), we do not recommend this, since it is an established standard in SQL to use the databases default case for function names. Since Exasol's default case is upper case, the name `HELLO` is the best choice.

If you look at the classes' implementation, you will find it trivial. Think of the `run` method like a Java application's typical main method. It has a fixed signature that you need to adhere to if you want Exasol to find the entry point of the script.

The `run` method's parameter `ExaMetadata metadata` holds a lot of the information about the script and the environment it runs in. The `ExaIterator context` parameter provides functions for scanning the input values and emitting complex output. Neither are used in this example, so we are going to accept their existence and otherwise ignore them for now.

The only real instruction in this example is returning the fixed string "Hello world!", which is exactly what you would expect from a "hello world" example.

## Running "Hello World"

Once you installed this extension, you can call it like any other scalar function:

```sql
SELECT hello()
```

The result that you should see in your SQL client has one row with one column of type `VARCHAR(20)` and a value of "Hello world!".

Congratulations, you successfully created and ran your first own Exasol extension in Java 🎉 

## Summary

You created the simplest possible scalar script in Exasol with Java and called it as part of an SQL query.