# TLS in User Defined Functions (UDFs)

## Before you Start

If you haven't already, I recommend you read our ["TLS introduction"](tls_in_udfs.md) to familiarize yourself with the basic concepts of TLS.

You should familiarize yourself with Exasol's [BucketFS](https://docs.exasol.com/db/latest/database_concepts/bucketfs/bucketfs.htm) filesystem.

You will also need:

1. A Java Development Kit (JDK 11 or later, we recommend [OpenJDK](https://openjdk.org/install/))
2. `keytool` program (comes with the JDK)
3. [`curl`](https://curl.se/) to access BucketFS and download file from the internet

## UDFs

A User Defined Function (short "UDF") is a way to extend the Exasol database with new features that can be called from within Exasol SQL statements. Basically. A UDF is a program that runs on the Exasol cluster in the context of an SQL query. The UDF gets its parameters from the SQL statement and its return values can be used by other parts of the query.

Each UDF runs in a sandbox that only lives in the context of a single statement. This ensures that UDF can't be used to escape the boundaries of the statement it belongs too and prevents data leakage.

While the sandbox makes the UDF more secure, it unfortunately makes using TLS harder.

## Language Containers

A UDF can be written in an arbitrary programming language but requires an appropriate runtime environment that can be provided as a so-called "Language Container". The Exasol database ships with a standard container providing the runtime environment for Java and Python. Other containers and updates for the standard container are [available for download on GitHub](https://github.com/exasol/script-languages-release/releases).

All UDFs require such a language container &mdash; except Lua UDFs, since Lua is embedded directly into the Exasol engine.

## Running UDFs

Whenever you run a SQL statement that contains a UDF, Exasol starts the corresponding language container. This serves both as a runtime environment and as a sandbox at the same time. Everything the UDF does happens inside the running container. Once the statement's execution is complete, Exasol terminates the container.

## Certificate Validation in UDFs

The introduction talked about how certificates are used to authenticate communication partners in TLS. As you might remember, you need a valid chain of certificates down to a certification agency (CA) that you trust.

Since the UDF runs in a sandbox, it can only see what the sandbox provides. That means without extra effort the UDF can only validate TLS certificates down to a CA certificate available in the sandbox (i.e. the language container). This is usually no problem if you want to access a service on the Internet, because in almost all cases services exposed to the internet have certificates that can be traced down to one of the well-established CAs. And the certificates for these CAs are shipped with the language container.

### Listing the Standard Certificates

This tutorial comes with a UDF that can be built into the JAR `tls-tutorial.jar`. You can install the JAR by following these steps:

1. Change to the base directory of this `exasol-java-tutorial`
2. If you haven't already, build the binaries (JAR files)
   ```shell
   mvn clean verify
   ```
3. Upload the JAR to the default bucket
   ```shell
   curl -v -X PUT http://w:<write-password>@<host>:2580/default/tls-tutorial.jar -T tls-tutorial/target/tls-tutorial.jar
   ```
   Note that you need to replace the write-password and host with their actual values.
4. Create a database schema `TLS_TUTORIAL` where you can put the UDF with `CREATE SCHEMA`
5. Register the UDF with `CREATE SCRIPT` 
6. Reference the uploaded JAR with the `%jar` directive in the script definition
7. Reference the entry class with the `%scriptclass` directive
8. Run the UDF inside a `SELECT` statement

For your convenience here is the complete SQL code for the steps above that need to be taken in a SQL client.
```sql
CREATE SCHEMA JAVA_TUTORIAL;

CREATE OR REPLACE JAVA SCALAR SCRIPT JAVA_TUTORIAL.CERTIFICATES()
EMITS (NAME VARCHAR(2000), ORG VARCHAR(2000), ORG_UNIT VARCHAR(2000),
       COUNTRY VARCHAR(2))
AS
    %jar /buckets/bfsdefault/default/tls-tutorial.jar;
    %scriptclass com.exasol.javatutorial.tls.Certificates;
/;

SELECT JAVA_TUTORIAL.CERTIFICATES() ORDER BY NAME;
```

Here are the first few rows of an example output.

| CN                             | O                          | OU       | C        |
|--------------------------------|----------------------------|----------|----------|
| AAA Certificate Services       | Comodo CA Limited          |          | GB       |
| ACCVRAIZ1                      | ACCV                       | PKIACCV  | ES       |
| Actalis Authentication Root CA | Actalis S.p.A./03358520967 |          | IT       |
| AffirmTrust Commercial         | AffirmTrust                |          | US       |
| &hellip;                       | &hellip;                   | &hellip; | &hellip; |

This script is especially helpful if you want to find out, which CA certificates are available in you UDF.

It goes without saying that if a certificate is missing, trust chains based on it cannot be verified and access to corresponding services will fail.

Each column corresponds to a path element for the so-called "distinguished name" (DN) of the certificate. Think of that name as a unique identifier. The elements are "common name" (CN), "organization" (O), "organizational unit" (OU), and "country" (C). 

### Self-issued Certificates and UDFs

If you want your UDF to connect to a service on your own premises, chances are that the corresponding TLS certificate was issued by your own CA or your organization's CA for that matter.

Now things are starting to get interesting. One thing is for sure, you won't find that certificate in the language container &mdash; unless you are planning to build your own, but that has its own learning curve and would also go way beyond the scope of this tutorial.

A better way to use your own certificates with UDFs is to create a truststore in [BucketFS](https://docs.exasol.com/db/latest/database_concepts/bucketfs/bucketfs.htm). BucketFS is a distributed filesystem that makes files available to all data nodes on an Exasol cluster. It is also the only filesystem accessible from a UDF, except the read-only filesystem of the language container.

### An Example Truststore For This Tutorial

You could start from scratch and create your very own CA certificate, but for the sake of keeping this tutorial compact, let's take an existing one and use that for our proof-of-concept.

The [Let's Encrypt Organization](https://letsencrypt.org) has a nice [page where you can see how they build their own certificate chains](https://letsencrypt.org/certificates/). You also find download links for their root CA certificates there.

The shell script below goes through the following steps:

1. Creates a directory where we store the files for our experiments
2. Change to the directory
3. Download the [ISRG Root X1 certificate](https://letsencrypt.org/certs/isrgrootx1.pem) from the Let's Encrypt website in PEM format
4. Create a new truststore and import the ISRG Root X1 certificate

```shell
export tls_tutorial_dir='/tmp/tls_tutorial'
export truststore_file='tls-tutorial-truststore'
mkdir -p "$tls_tutorial_dir"
cd "$tls_tutorial_dir"
curl -O https://letsencrypt.org/certs/isrgrootx1.pem
keytool -import -file 'isrgrootx1.pem' -alias 'ISRG_Root_X1' -keystore "$truststore_file"
```

You have to provide a password now to protect your new keystore. For this tutorial simply use `tutorial`. Obviously in a real world scenario, you would generate a non-guessable password with your favorite password manager.

The directory now contains the downloaded PEM file and the truststore that we just created. You can verify that the truststore only contains the certificate we just imported like this:

```shell
keytool -list -keystore "$truststore_file"
```

This is what you should see (import date will be different in your case of course):

```
Keystore type: PKCS12
Keystore provider: SUN

Your keystore contains 1 entry

isrg_root_x1, Nov 28, 2022, trustedCertEntry,
Certificate fingerprint (SHA-256): 96:BC:EC:06:26:49:76:F3:74:60:77:9A:CF:28:C5:A7:CF:E8:A3:C0:AA:E1:1A:8F:FC:EE:05:C0:BD:DF:08:C
```

Congratulations, you just created your very own Java truststore. Now, let's use it.

### Uploading the Truststore to BucketFS

We are going to use [`curl` to upload the truststore](https://docs.exasol.com/db/latest/database_concepts/bucketfs/file_access.htm#cURL) file to the default bucket.

```shell
curl -v -X PUT "http://w:<write-password>@<host>:2580/default/$truststore_file" -T "$truststore_file"
```

We use `curl` in verbose mode (`-v`), so that we can see if the upload actually succeeded by checking for this line in the output:

    HTTP/1.1 200 OK

Additionally, we check the contents of the default bucket to verify we have everything in place:

```shell
curl "http://localhost:2580/default"
```

This should list the following items:

1. A script language container `EXClusterOS/ScriptLanguages<...>.tar.gz`
2. The tutorial JAR file `tls-tutorial.jar`
3. The truststore `tls-tutorial-truststore`

### Overriding the Default Trust Store

While Java comes with a default trust store, you can override it using JVM arguments. To do this, you have the following properties:

| Property                           | Meaning                        | Example                                         |
|------------------------------------|--------------------------------|-------------------------------------------------|
| `javax.net.ssl.trustStore`         | Path to the trust store file   | `/etc/ssl/certs/java/cacerts` (on Ubuntu Linux) | 
| `javax.net.ssl.trustStorePassword` | Password for the trust store   | `changeit`                                      |

If you want to inject JVM arguments into a UDF, you can use the `%jvmoption` directive in the [`CREATE SCRIPT`](https://docs.exasol.com/db/latest/sql/create_script.htm) statement.

```sql
CREATE OR REPLACE JAVA SCALAR SCRIPT JAVA_TUTORIAL.CUSTOM_CERTIFICATES()
EMITS (CN VARCHAR(2000), O VARCHAR(2000), OU VARCHAR(2000), C VARCHAR(2)) AS
    %jvmoption -Djavax.net.ssl.trustStore=/buckets/bfsdefault/default/tls-tutorial-truststore -Djavax.net.ssl.trustStorePassword=tutorial;
    %jar /buckets/bfsdefault/default/tls-tutorial.jar;
    %scriptclass com.exasol.javatutorial.tls.Certificates;
/;

SELECT JAVA_TUTORIAL.CUSTOM_CERTIFICATES();
```

At this point you should see only the one entry that we added to our new trusstore earlier.

| CN           | O                                | OU  | C   |
|--------------|----------------------------------|-----|-----|
| ISRG Root X1 | Internet Security Research Group |     | US  |

### Trust Store Password and Protecting the Truststore

If you followed the tutorial closely to that point, you might have wondered about the significance of the truststore password. Is it dangerous that you need to know it? Should you change the default password?

Let's start by looking at what you want to protect here. IT security always falls into at least one of the following three categories: confidentiality, integrity and availability.

Availability is not our main concern here, since as long as Exasol runs, BucketFS is available. And if Exasol is down, BucketFS availability is the least of your problems.

By definition certificates are public information. If your protection scheme depends on keeping certificates confidential, your process is broken. That means there is no point in protecting the certificates from being read.

Integrity on the other hand matters here. If an attacker manages to sneak a malicious certificate into your truststore, your certificate validation process is compromised.

Does the truststore password help here? Not really. Since the same password is used for reading and writing, you cannot keep it completely secret. That would only be the case if the truststore itself used asymmetric encryption. So if the truststore password isn't the answer, you need to protect the file instead.

In an operating system this is usually achieved by restricting write access to the truststore to privileged users. You can emulate this for UDFs.

1. [Create a private bucket](https://docs.exasol.com/db/latest/database_concepts/bucketfs/create_new_bucket_in_bucketfs_service.htm) that only privileged users (e.g. the DBA) have write access to
2. Chose a non-guessable write-password for the bucket
3. Chose a non-guessable read-password for the bucket that differs from the write-password
4. [Create a connection object for the bucket](https://docs.exasol.com/db/latest/database_concepts/bucketfs/database_access.htm) with the read-password
5. [Grant access to the connection object](https://docs.exasol.com/db/latest/sql/grant.htm?Highlight=connection) to the users that will use the truststore in their UDFs
6. Upload the truststore file to that bucket using the write-password

In summary, we propose to live with a simple truststore password and to refer to the items in the list above in order to gain an acceptable level of security.
## Summary

* User Defined Functions run in a sandbox.
* The only filesystem the sandbox sees is BucketFS.
* If you want to use your own Certification Agency (CA), we recommend that you generate a new Java truststore that only contains the CAs you trust.
* Upload it to a private bucket, create a connection object to allow UDFs to read it.
* Then grant access to that connection object to users who should be allowed to use that UDF.
* Use the `CERTIFICATES` function from this tutorial to verify your installation of the truststore.