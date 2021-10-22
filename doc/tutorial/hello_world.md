# Hello world!

In this tutorial we are going to cover the absolute minimal amount of steps required to get a running "hello world" extension into Exasol.

# Prerequisites

You need a running Exasol instance and the simplest possible way to get there is to use our [`dockerdb`](https://github.com/exasol/docker-db) images and start Exasol in a Docker container.

```bash
docker run --name exasoldb -p 127.0.0.1:8563:8563 --detach --privileged --stop-timeout 120  exasol/docker-db:7.1.2
```

You can check the latest `docker-db` version in the [release list](https://github.com/exasol/docker-db/releases).

# Creating a Minimal Scalar Script

The absolute simplest possible extension that you can write in Java, is a [function script](https://docs.exasol.com/database_concepts/udf_scripts/java.htm).

Once you installed this extension, you can call it like any other scalar function:

```sql
SELECT hello()
```