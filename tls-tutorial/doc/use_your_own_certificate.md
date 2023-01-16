# Using Your Own Certificates

In this hands-on tutorial we will go over everything that is needed to get an `IMPORT` command running in a scenario where the TLS certificates involved are issued by yourself (or the organization you are part of).

## Goals

The goals of this tutorial are to

* understand the different TLS certificate types and be able to create them
* practice installing certificates
* run `IMPORT` on Exasol to copy data from MySQL over a TLS connection

This will take you through tasks usually performed by network admins and database admins.

## Before You Start 

If you haven't already we highly recommend you familiarize yourself with the general TLS topic by reading the [TLS ntroduction](tls_introduction.md).

## What you Will Need

To follow this tutorial, you need 

* at least 10 GiB free space on your filesystem
* [Ubuntu](https://ubuntu.com/) machine or VM
* [OpenSSL](https://www.openssl.org/)
* [Docker](https://www.docker.com/)
* Exasol's [docker-db](https://github.com/exasol/docker-db)
* [MySQL docker image](https://hub.docker.com/_/mysql/)

## Installing the Software

1. Update the machine you are using
    ```shell
    sudo apt update
    sudo apt upgrade
    ```
1. Install Docker
   ```shell
   sudo apt install docker.io
   ```
1. Install Open SSL.
    Note that on Ubuntu OpenSSL should be preinstalled. If not run:
    ```shell
    sudo apt install openssl
    ```
2. Create a directory where we will keep the files that we are creating in the course of this project.
    ```shell
    tutorial_dir="$HOME/tutorials/tls_with_exasol/import"
    mkdir -p "$tutorial_dir"
    cd "$tutorial_dir"
    ```

## Creating and Installing the Certificates

In this part of the tutorial we will create the required TLS certificates and install them on Exasol and MySQL.

### Creating Your own Certification Agency (CA)

To get that right out of the way, yes, in a normal organization, you already have a Certification Agency (CA). But for the purpose of this tutorial, let's pretend you don't.

That means we will create our own, and that in turn gives us the opportunity to create the server certificates we are going to use quickly.

The script below takes you through the following steps:

1. Create a key pair for the CA
    ```shell
    openssl genrsa -aes256 -out ca.key 4096
    ```
2. You will be prompted for a passphrase. Use `tutorial` for this demonstration.
    In a real world scenario you would obviously choose a secure, non-guessable passphrase.
3. Create a CA certificate
   ```shell
    openssl req -x509 -new -nodes -key ca.key -sha256 -days 365 -out ca.crt -subj '/CN=TLS Tutorial CA/C=DE/L=Bavaria/O=Tutorial Organization'
    ```
4. Since the last step requires reading the CA key, you will need to enter the passphrase again here

Let's talk about what the individual parameters of the `openssl` mean:

`req`
: This subcommand takes care of everything that has to do with a certificate signing request. We will talk about [Singing Requests](#creating-a-singing-request-for-a-server-certificate) later in detail.

`-x509`
: This switch tells OpenSSL to immediately execute the signing request, so that the result will be a self-signed certificate. It's a shortcut.

`-new`
: Create a fresh certificate instead of signing an existing one.

`-key <path>`
: Path to the file that holds the key to sign the certificate.

`-sha256`
: Use the [SHA256 hash algorithm](https://en.wikipedia.org/wiki/SHA-2) to generate a hash from the certificate contents.

`-days <number>`
: Number of days until the certificate will expire.

`-out <path>`
: Path to the file that will contain the newly created certificate.

`-subj <key=value[/key=value]*>`
: Subject name of the certificate. Usually in form of a LDAP-style [distinguished name](https://www.rfc-editor.org/rfc/rfc4514). You can find an attribute list in [RFC4519](https://www.rfc-editor.org/rfc/rfc4519#page-2).

#### Reading the Certificate

Let's face it. Sooner or later you will have to read through the contents of TLS certificates in order to debug why you don't get a TLS connection working. So why not get practicing that out of the way right now while you are still relaxed &mdash; instead of when you are fixing network connections in a production system.

The command to view the contents of a certificate file is comparably simple:

```shell
openssl x509 -in ca.crt -text -noout
```

Here's what the parameters mean:

`x509`
: subcommand for all actions related to managing [X.509 certificates](https://www.rfc-editor.org/rfc/rfc5280).

This will print out a longish text representation of the certificate like the one below. The example output below is shortened (`[...]`) where you normally have long rows of hex dumps.

```
Certificate:
    Data:
        Version: 3 (0x2)
        Serial Number:
            0d:fc:[...]
        Signature Algorithm: sha256WithRSAEncryption
        Issuer: CN = TLS Tutorial CA, C = DE, L = Bavaria, O = Tutorial Organization
        Validity
            Not Before: Dec  9 14:37:35 2022 GMT
            Not After : Dec  9 14:37:35 2023 GMT
        Subject: CN = TLS Tutorial CA, C = DE, L = Bavaria, O = Tutorial Organization
        Subject Public Key Info:
            Public Key Algorithm: rsaEncryption
                RSA Public-Key: (4096 bit)
                Modulus:
                    00:ba:[...]
                Exponent: 65537 (0x10001)
        X509v3 extensions:
            X509v3 Subject Key Identifier: 
                14:44:73:3F:17:A5:F8:74:93:05:07:0D:A9:FA:7F:BD:26:E1:54:E0
            X509v3 Authority Key Identifier: 
                keyid:14:44:73:3F:17:A5:F8:74:93:05:07:0D:A9:FA:7F:BD:26:E1:54:E0

            X509v3 Basic Constraints: critical
                CA:TRUE
    Signature Algorithm: sha256WithRSAEncryption
         6e:8d:[...]
```

The output tells us that we are locking at an X.509 certificate in version 3. The signature consists of an SHA256 hash that is signed with an RSA encryption key. That's the key we created as the very first thing.

If you are wondering why issuer and subject are the same, then remember, we are talking about a **root** CA certificate here, so that is a valid combination.

The validity is a year as we defined. It is represented here by a start and end date. The start date is the date and time we created the certificate.

The public key part is the most interesting part of the certificate, since that is what clients will use to validate certificates that *claim* to be signed by this CA. We are talking about asymmetric cryptography here and only the actual CA holds the private signing key. That means if the client is able to validate a given signature with the CA's known and trusted public key, that signature is authentic.

[Subject key identifier](https://www.rfc-editor.org/rfc/rfc3280#section-4.2.1.2) and authority key identifier are unique identifiers that are used in certificate chaining. Note that in our root CA example they are identical.

The `critical` flag after an extension definition means that any system using the certificate _must_ know and process it. The system can ignore extensions that are not marked like this. Case in point: the key identifiers are non-critical because there is a fallback in the form of issuer and subject, although the IDs are obviously the better alternative for machine evaluation.

The `CA:TRUE` entry is pretty self-explaining. You are looking at a CA certificate.

Finally, there is a block that contains the signature of the certificate, which in this case is not particularly useful, since it is self-signed. Since that is the beginning of the chain, and it contains the public key that is required to validate the signature, you have the dreaded chicken-of-the-egg problem of all IT security here. That means the *root* CA certificates must be installed on a client by means of a trusted channel. 

### Creating a Singing Request for a Server Certificate

Services that are offered via TLS present a server certificate to the client. The client reads the certificate contents and uses CA certificates that are installed locally to verify the signature in the server certificate.

Note this in this step you will create a certificate singing request (CSR) (see [RFC2986](#rfc2986)) instead of an actual certificate. Think of the signing request a precursor of a signed certificate.

1. Create a key pair _without_ passphrase for the server:
    ```shell
    openssl genrsa -out server.key 4096
    ```
2. You will be prompted for a password. Use `tutorial` for this demonstration.
   In a real world scenario you would obviously choose a secure, non-guessable password.
3. Create a singing request
   ```shell
   openssl req -new -nodes -key server.key -sha256 -out server.csr -subj '/CN=TLS Tutorial MySQL Server/C=DE/L=Bavaria/O=Tutorial Organization'
    ```

Note how the `-x509` switch is gone from the command. This time we really want a signing request, not a self-signed certificate.

If you look real close, you might wonder why there is no `-days` switch here. The reason is simple: the CA decides how long a certificate it creates is valid. Not the requester.

### Reading a Singing Request

Let's take a look at the generated singing request to get a better understanding of its contents.

```shell
openssl x509 -in ca.crt -text -noout
```

Produces an output that looks like this (again we shortened the hex dumps):

```
Certificate Request:
    Data:
        Version: 1 (0x0)
        Subject: CN = TLS Tutorial CA, C = DE, L = Bavaria, O = Tutorial Organization
        Subject Public Key Info:
            Public Key Algorithm: rsaEncryption
                RSA Public-Key: (4096 bit)
                Modulus:
                    00:99:af[...]
                Exponent: 65537 (0x10001)
        Attributes:
            a0:00
    Signature Algorithm: sha256WithRSAEncryption
         8c:33:[...]
```

As you can see, there is not much to it. It looks like a half-finished certificate. Just a text representation of the data you provided on the commandline.

The modulus is a property that the private and public key of a server share. Its purpose is allowing to verify key authenticity.

If you are curious, you can check the modulus of the server key like this:

```shell
openssl rsa -in server.key -text
```

When you do this on your machine you will find that the output matches the modulus in the CSR.

```
RSA Private-Key: (4096 bit, 2 primes)
modulus:
    00:99:af:[...]
[...]
```

The most important puzzle piece in the signing request is that it contains the public key of the requester. In our case the server public key. Without that users of the certificate would not be able to verify the authenticity of the TLS connection.

You can export the public key from the signing request like this:

```shell
openssl req -in server.csr -pubkey -noout
```

If you compare it to the public key contained in the server key pair, you will find it is identical.

```shell
openssl rsa -in server.key -pubout
```

### Singing the Server Certificate

That is something that the organization owning the server would normally send to the certification agency. The request contains everything the CA needs to create a signed certificate.

Since in our tutorial you own both the server and the CA, you can play both parts.

We earlier briefly already touched the topic of certificate extensions. To define which extensions you want on the server certificate signed by the CA, you need to create a small [configuration file](#x509v3config).

```shell
echo '[extensions]
keyUsage = critical, nonRepudiation, digitalSignature, keyEncipherment, keyAgreement
basicConstraints = CA:false' > server_cert_extensions.cfg
```

Let's look at the component of the configuration options.

`[extensions]`
: Is a section header. This header can be used for find a set of options in the configuration file.

`keyUsge`
: Defines what a client should accept the certificate for. If something is not listed here, the client _must_ (enforced by the `critical` modifier) refuse any attempts to use the key for it. Case in point, this server key may not sign certificates.

: [Non-repudiation](#non-repudiation) in this context means a cryptographic method to make sure that neither sender nor recipient of information can deny to have processed that information.

: The key can also be used for creating a digital signature and encipher other keys as well as key agreement. If you are asking yourself why payload data encryption is not mentioned, remember that TLS uses asymmetric cryptography only until the symmetric session key is exchanged. Actual data payload is encrypted symmetrically.

`basicConstraints`
: We explicitly add a constraint here that this configuration is not CA certificates. You could drop that option if you wanted, because this is the default for certificates. 

The following command creates a certificate from the singing request and signs it with the private key of the CA.

```shell
openssl x509 -req -in server.csr -CA ca.crt -CAkey ca.key -CAcreateserial -out server.crt -days 90 -sha256 -extfile server_cert_extensions.cfg -extensions extensions
```

You will be prompted for the passphrase of the CA key.

As always let's take a peek at the certificate that we just created.

```shell
openssl x509 -in server.crt -text -noout
```

Your server certificate should now look similar to the output below:

```
Certificate:
    Data:
        Version: 3 (0x2)
        Serial Number:
            2c:18:91:ff:3a:b3:31:38:a4:16:fc:05:f3:ed:18:74:fd:c5:57:b8
        Signature Algorithm: sha256WithRSAEncryption
        Issuer: CN = TLS Tutorial CA, C = DE, L = Bavaria, O = Tutorial Organization
        Validity
            Not Before: Jan 12 09:07:04 2023 GMT
            Not After : Apr 12 09:07:04 2023 GMT
        Subject: CN = TLS Tutorial MySQL Server, C = DE, L = Bavaria, O = Tutorial Organization
        Subject Public Key Info:
            Public Key Algorithm: rsaEncryption
                RSA Public-Key: (4096 bit)
                Modulus:
                    00:99:af:[...]
                Exponent: 65537 (0x10001)
        X509v3 extensions:
            X509v3 Key Usage: critical
                Digital Signature, Non Repudiation, Key Encipherment, Key Agreement
            X509v3 Basic Constraints: 
                CA:FALSE
    Signature Algorithm: sha256WithRSAEncryption
         75:41:37:[...]
```

No we are all set to install the certificates.

### Certificate Chains

For the sake of completeness it should be mentioned here that out in the wild server certificates are usually signed by subordinate CAs. So the client has to follow the certificate chain all the way down to a CA it trusts. Servers can even present server certificates that are combined with some intermediate CA certificates to fill the gaps. Even then the fact still remains, that client must know and trust the last CA in the chain.

We keep things simple here though, since this is just a tutorial meant to convey the general idea of server certificate validation.

## Setting up MySQL

1. Install MySQL client and server
   ```shell
   sudo apt install mysql-client mysql-server
   ```
2. Log into MySQL as MySQL root user (on Ubuntu now requires `sudo`)
   ```shell
   sudo mysql -u root
   ```
3. Create a database `shapes` and a user `tutorial_user` for our import experiment
   ```sql
   create database shapes;
   create user 'tutorial_user' identified by 'tutorial';
   grant all on shapes.* to tutorial_user;
   exit
   ```
4. Login as a `tutorial_user`
   ```sql
   mysql -u tutorial_user -p
   ```
5. Populate the database
   ```sql
   create table shapes.shapes (name VARCHAR(40), corners int);
   insert into shapes.shapes values ('point', 1), ('line', 2), ('triangle', 3), ('rectangle', 4);
   exit
   ```

As you can tell we won't win any price for most original database usage here, but that is not the point of the tutorial.

### Installing Certificates in MySQL

By default, the MySQL server on Ubuntu comes with a self-signed certificate. Our next job will be to use the certificates we previously created.

1. Install the CA certificate centrally:
   ```shell
   sudo cp ca.crt /usr/local/share/ca-certificates/exasol_tutorial_ca.crt
   sudo update-ca-certificates
   ```
2. Verify that it was installed:
   ```shell
   ls -l /etc/ssl/certs/exasol*
   ```
   Must output something like:
   ```
   lrwxrwxrwx 1 root root 55 Jan 12 11:43 /etc/ssl/certs/exasol_tutorial_ca.pem -> /usr/local/share/ca-certificates/exasol_tutorial_ca.crt
   ```
   Note how the symbolic link changes the file suffice from `.crt` to `.pem`
3. Install the server certificate and key
   ```shell
   sudo mkdir -p /etc/mysql/certs
   sudo cp server.crt /etc/mysql/certs/
   sudo cp server.key /etc/mysql/certs/
   sudo chown mysql:mysql /etc/mysql/certs/server.key
   ```

Note that we need `sudo` privileges here, since we need to install the certificates in a central locations that belong to the `root` user.

Additionally, we give the private key to the MySQL user. We don't want anyone else to be able to read it, so weakening permissions on that file is not an option.

You can check ownership and permissions like this:

```shell
ls -l /etc/mysql/certs/
```

This should list two files:

```
-rw-r--r-- 1 root  root  1919 Jan 12 11:56 server.crt
-rw------- 1 mysql mysql 3326 Jan 12 11:56 server.key
```

Now edit the `[mysqld]` section of the MySQL configuration file and *add* the following lines as `root` user:

```
[mysqld]
ssl
ssl-ca=/etc/ssl/certs/exasol_tutorial_ca.pem
ssl-cert=/etc/mysql/certs/server.crt
ssl-key=/etc/mysql/certs/server_key.pem
```

Also, we need to enable Exasol later to access the MySQL server. Since that will not be access from `localhost` you need to accept all interfaces.

```
bind-address = 0.0.0.0
```

Restart the MySQL server daemon so that the changed configuration takes effect.

```shell
sudo service mysql restart
```

### Examining the TLS Connection to the MySQL Server

At this point you should be able to use OpenSSL's `sclient` to look at the TLS connection:

```shell
openssl s_client -starttls mysql -connect localhost:3306
```

This results in a log text that tells you all the details about the TLS connection.

Let's look at some of the more interesting parts. We'll start with the certificate chain.

```
Certificate chain
 0 s:CN = TLS Tutorial MySQL Server, C = DE, L = Bavaria, O = Tutorial Organization
   i:CN = TLS Tutorial CA, C = DE, L = Bavaria, O = Tutorial Organization
 1 s:CN = TLS Tutorial CA, C = DE, L = Bavaria, O = Tutorial Organization
   i:CN = TLS Tutorial CA, C = DE, L = Bavaria, O = Tutorial Organization
```

As you can see the chain has two links. The server certificate (denoted with index zero) and the issuing CA (index one).

The abbreviation `s:` stands for "subject name", `i:` for "issuer". You can also see that the CA certificate is self-signed.

We also learn a few details about the cipher and server public key. This helps us judge, if the cryptography used is still secure enough:

```
New, TLSv1.3, Cipher is TLS_AES_256_GCM_SHA384
Server public key is 4096 bit
```

At the time of this writing TLS 1.3 (January 2023), AES 256 and RSA 4096 are all considered secure.

As mentioned before, asymmetric cryptography is only used to establish the initial connection. Once that is done the TLS communication partners negotiate a symmetric session. You can see this here: 

```
Post-Handshake New Session Ticket arrived:
SSL-Session:
    Protocol  : TLSv1.3
    Cipher    : TLS_AES_256_GCM_SHA384
    Session-ID: DAB72937942090F3588FD90368047E7B90361EAE4427DD326530F7CE6041174E
    Session-ID-ctx: 
    Resumption PSK: F2055F6F181E774D3B05EBF321662C702F32DCB48D057B9C928FF35FC217C5A167773EF4715339E34B5061C113511A00
    PSK identity: None
    PSK identity hint: None
    SRP username: None
    TLS session ticket lifetime hint: 300 (seconds)
    TLS session ticket:
    0000 - 4b 25 3d [...]
```

[Session Tickets](#rfc5077) are a way to allow resuming sessions without keeping server-side state per client. If you imagine how many clients a TLS-capable server might have to handle under high load, this is quite useful.

The most important part here are the cipher and the ticket lifetime of 5 minutes.

**Side-note:** if you ever experience TLS connections breaking of after a certain time, you should check first this session ticket lifetime and then any intermediate network components like firewalls. Sometimes they interfere with the connection renewal process.

Finally, you can connect the MySQL client to the server using TLS:

```shell
mysql -u tutorial_user -p --ssl-mode=REQUIRED -u tutorial_user
```

The source side of our import via TLS is now set up and ready to use.

## Setting up Docker

For the next steps we need a Docker setup

1. Install Docker
    ```shell
    sudo apt install docker.io
    ```
2. Add your current user to the `docker` group
    ```shell
    sudo usermod -aG docker "$USER"
    ```
3. Log out and back in to apply the group change
4. Verify that your current user is in the `docker` group
    ```shell
   groups
    ```

## Installing Exasol via Docker

Exasol's [docker-db](https://github.com/exasol/docker-db) provides a Docker container that is quite convenient for experiments like our tutorial.

The following docker command downloads the container's layers and afterwards starts the container. Expect a couple of minutes until the download and database start are done.

```shell
docker run --name exasoldb -p 8563:8563 -p 2580:2580 --detach --privileged --stop-timeout 120  exasol/docker-db:7.1.17
```

There are two ports forwarded to the host:

- 8563: database port
- 2580: [BucketFS] port

### Installing the MySQL JDBC driver in Exasol

[Import](https://docs.exasol.com/db/latest/sql/import.htm) into Exasol requires that the database driver of the source database (MySQl in our case) is installed in Exasol.

Installation in the Docker variant requires uploading the driver to a [BucketFS] bucket and updating a configuration file.

1. Download the [MySQL Connector] (JDBC driver)
   ```shell
   wget https://dev.mysql.com/get/Downloads/Connector-J/mysql-connector-j-8.0.31.tar.gz
   ```
2. Get the ID of the Exasol Docker container
   ```shell
   container_id=$(docker ps -a | grep exasol | sed -e's/ .*//')
   ```
3. Get the write-password of the default Bucket in BucketFS
   ```shell
   write_pwd=$(docker exec -it "$container_id" cat /exa/etc/EXAConf | grep WritePasswd | sed -e's/^.* = //' -e's/[\n\r]//g' | base64 --decode)
   ```
4. Copy the driver to the default Bucket
   ```shell
   curl -vX PUT -T mysql-connector-j-8.0.31/mysql-connector-j-8.0.31.jar "http://w:$write_pwd@localhost:2580/default/drivers/jdbc/mysql-connector-j-8.0.31.jar"
   ```
5. Create a driver configuration (`settings.cfg`)
   ```shell
   echo 'DRIVERNAME=MYSQL_JDBC
   JAR=mysql-connector-j-8.0.31.jar
   DRIVERMAIN=com.mysql.cj.jdbc.Driver
   PREFIX=jdbc:mysql:
   NOSECURITY=YES
   FETCHSIZE=100000
   INSERTSIZE=-1' > settings.cfg
   ```
6. Upload the configuration to `drivers/jdbc` in the default bucket:
   ```shell
   curl -vX PUT -T settings.cfg "http://w:$write_pwd@localhost:2580/default/drivers/jdbc/settings.cfg"
   ```
7. Verify this installation:
   ```shell
   curl http://localhost:2580/default
   ```
   Must produce a listing that looks similar to this:
   ```
   EXAClusterOS/ScriptLanguages-standard-EXASOL-7.1.0-slc-v4.0.0-CM4RWW6R.tar.gz
   drivers/jdbc/mysql-connector-j-8.0.31.jar
   drivers/jdbc/settings.cfg
   ```
   
### Copying the Certificate to Exasol

**Information:** this part will be added in a separate pull request.

## Run the Import

In this last part of the tutorial, we import the data from the `shapes` database and `shapes` table in MySQl into Exasol.

One last piece of preparation is that you get the IP address of the Docker host (i.e. the Ubuntu machine you run the tutorial on).

```shell
ip address show docker0 | grep inet
```

Please, note down the IP address. You will need it for the `IMPORT` command.

We need a why to talk to Exasol, so please install the [usql](https://github.com/xo/usql) commandline SQL client on the Ubuntu machine. Current versions come with the [Exasol Go driver preinstalled](https://github.com/xo/usql#supported-database-schemes-and-aliases).

   ```shell
   wget https://github.com/xo/usql/releases/download/v0.13.5/usql_static-0.13.5-linux-amd64.tar.bz2
   tar --bzip2 -xvf usql_static-0.13.5-linux-amd64.tar.bz2
   ```

Next, connect to the Exasol server

```shell
./usql_static exa://sys:exasol@localhost?validateservercertificate=0
```

Since we forwarded the default port, we can directly connect to `localhost`, even if the Exasol database is running in a Docker container.

You are probably asking yourself why we skip certificate validation here. The reason is that we otherwise would have to outfit Exasol with a server certificate too and that is beyond the scope of this particular tutorial for now (later versions might add this).

In order to run the import, we need a target table on Exasol, so let's prepare it.

```sql
CREATE SCHEMA target_schema;
CREATE TABLE target_schema.shapes_target(name VARCHAR(40), corners INT);
```

You are now set up and ready to run the import.

```shell
IMPORT INTO target_schema.shapes_target
FROM JDBC AT 'jdbc:mysql://<ip-address-of-docker-host>/shapes?sslMode=REQUIRED'
USER 'tutorial_user' IDENTIFIED BY 'tutorial'
STATEMENT 'SELECT * FROM shapes';
```

Let's look at the individual parts of this SQL statement.

`IMPORT INTO <target-schema>.<target-table>`
: Tells Exasol to run an import into an existing schema and table.

`FROM <source> AT <url>`
: Defines from which datasource the data should be copied. Here a JDBC URL with server IP address and a configuration parameter.

: As you probably guessed, the option `sslMode=REQUIRED` [enforces a TLS connection](https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-connp-props-security.html#idm45913412865616) from the MySQL JDBC driver to the MySQL server.

`USER <username> IDENTIFIED BY <password>`
: Contains the credentials that are used to connect to the source. In our case the database user and password on MySQL.

`STATEMENT '<sql-statement-to-run-on-source>'`

: Defines which statement should be executed on the data source. The result set of this statement is what gets imported.

Now, check the results in the target table:

```shell
SELECT * FROM target_schema.shapes_target;
```

This must yield the following result

```
   name    | corners 
-----------+---------
 point     |       1
 line      |       2
 triangle  |       3
 rectangle |       4
(4 rows)

   ```
   
Congratulations, you just imported data from MySQL via a TLS connection.

## Conclusion

In this tutorial you learned how to create, read and debug TLS certificates for a Certification Agency (CA) and for a server. You installed the server certificate machines to allow certificate chain validation. You installed the server certificate on the source server and used `s_client` to look at the TLS connection. Finally, you ran an import that transferred data from a source database via TLS to Exasol.

## References

###### Non-Repudiation

[non-repudiation](https://csrc.nist.gov/glossary/term/non_repudiation), NIST Glossary, NIST

###### RFC2986

[PKCS #10: Certification Request Syntax Specification
Version 1.7](https://www.rfc-editor.org/rfc/rfc2986), M. Nystrom, B. Kalinski, Nov. 2000

###### RFC7468

[Textual Encodings of PKIX, PKCS, and CMS Structures](https://www.rfc-editor.org/rfc/rfc7468), S. Josefsson, S. Leonard, April 2015


###### RFC5077

[Transport Layer Security (TLS) Session Resumption without
Server-Side State](https://www.rfc-editor.org/rfc/rfc5077), J. Salowey, H. Zhou, P. Eronen, H. Tschofenig, January 2008

###### x509v3_config

[X509 V3 certificate extension configuration format](https://www.openssl.org/docs/manmaster/man5/x509v3_config.html), OpenSSL Project


[BucketFS]: https://docs.exasol.com/db/latest/database_concepts/bucketfs/bucketfs.htm
[MySQL Connector]: (https://dev.mysql.com/get/Downloads/Connector-J/mysql-connector-j-8.0.31.tar.gz)