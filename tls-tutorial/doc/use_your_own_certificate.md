# Using Your Own Certificates

In this hands-on tutorial we will go over everything that is needed to get an `IMPORT` command running in a scenario where the TLS certificates involved are issued by yourself (or the organization you are part of).

## Goals

The goals of this tutorial are to

* understand the different TLS certificate types and be able to create them
* practice installing certificates
* run `IMPORT` from MySQL over a TLS connection into Exasol

This will take you through tasks usually performed by network admins and database admins.

## Before You Start 

If you haven't already we highly recommend you familiarize yourself with the general TLS topic by reading the [TLS ntroduction](tls_introduction.md).

## What you Will Need

To follow this tutorial, you need 

* at least 10 GiB free space on your filesystem
* Linux machine or VM
* [OpenSSL](https://www.openssl.org/)
* [Docker](https://www.docker.com/)
* Exasol's [docker-db](https://github.com/exasol/docker-db)
* [MySQL docker image](https://hub.docker.com/_/mysql/)

## Installing the Software

1. General YM support
    ```shell
    sudo apt install gpm qemu-guest-agent
    ```
1. Install Open SSL
    On Debian / Ubuntu OpenSSL should be preinstalled. If not run:
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
    ca_key_file="tls_tutorial_ca.key"
    openssl genrsa -aes256 -out "$ca_key_file" 4096
    ```
2. You will be prompted for a password. Use `tutorial` for this demonstration.
    In a real world scenario you would obviously choose a secure, non-guessable password.
3. Create a CA certificate
   ```shell
    ca_cert_file="tls_tutorial_ca.crt"
    openssl req -x509 -new -nodes -key "$ca_key_file" -sha256 -days 365 -out "$ca_cert_file" \
    -subj '/CN=TLS Tutorial CA/C=DE/L=Bavaria/O=Tutorial Organization'
    ```
4. Since the last step requires reading the CA key, you will need to enter the password again here

Let's talk about what the individual parameters of the `openssl` mean:

`req`
: This subcommand takes care of everything that has to do with a certificate signing request

`-new`
: Create a fresh certificate instead of signing an existing one

`-key <path>`
: Path to the file that holds the key to sign the certificate

`-sha256`
: Use the [SHA256 hash algorithm](https://en.wikipedia.org/wiki/SHA-2) to generate a hash from the certificate contents

`-days <number>`
: Number of days until the certificate will expire

`-out <path>`
: Path to the file that will contain the newly created certificate

`-subj <key=value[/key=value]*>`
: Subject name of the certificate. Usually in form of a LDAP-style [distinguished name](https://www.rfc-editor.org/rfc/rfc4514). You can find an attribute list in [RFC4519](https://www.rfc-editor.org/rfc/rfc4519#page-2)

#### Reading the Certificate

Let's face it. Sooner or later you will have to read through the contents of TLS certificates in order to debug, why you don't get a TLS connection working. So why not get practicing that out of the way now while you are still relaxed instead of when you are fixing network connections in a production system.

The command to view the contents of a certificate file is comparably simple:

```shell
openssl x509 -in "$ca_cert_file" -text -noout
```

Here's what the parameters mean:

`x509`
: subcommand for all actions related to managing [X.509 certificates](https://www.rfc-editor.org/rfc/rfc5280)

This will print out a longish text representation of the certificate like the one below. The example is shortened (`[...]`)where you have long rows of hex dumps.

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

The output tells us that we are locking at an X.509 certificate in version 3. The Signature consists of an SHA256 hash that is signed with an RSA encryption key. That's the key we created as the very first thing.

If you are wondering why issuer and subject are the same, then remember, we are talking about a **root** CA certificate here, so that is a valid combination.

The public key part is the most interesting part of the certificate, since that is what clients will use to validate certificates that *claim* to be signed by this CA. We are talking about asymmetric cryptography here and only the actual CA holds the private signing key. That means if the client is able to validate a given signature with the CAs known and trusted public key, that signature is authentic.

[Subject key identifier](https://www.rfc-editor.org/rfc/rfc3280#section-4.2.1.2) and authority key identifier are unique identifiers that are used in certificate chaining. Note that in our root CA example they are identical.

The `critical` flag after an extension definition means that any system using the certificate must know and process it. It can ignore extensions that are not marked like this. Case in point: the key identifiers are non-critical because there is a fallback in issuer and subject, although the IDs are obviously the better alternative for machine evaluation.

The `CA:TRUE` entry is pretty self-explaining. You are looking at a CA certificate.

Finally, there is a block that contains the signature of the certificate, which in this case is not particularly useful, since it is self-signed. Since that is the beginning of the chain, and it contains the public key that is required to validate the signature, you have the dreaded chicken-of-the-egg problem of all IT security here. That means the *root* CA certificates must be installed on a client by means of a trusted channel. 

### Creating a Server Certificate

Services that are offered via TLS present a server certificate to the client. The client reads the certificate contents and uses CA certificates that are installed locally to verify the signature in the server certificate.

In our example we will sign the server certificate directly with our CA certificate.

1. Use the private CA key to sign a CA certificate
2. Create a server certificate for the MySQL server
3. Sign the server certificate with the CA

```shell
ca_cert_file="tls_tutorial_ca.crt"
openssl req -x509 -new -nodes -key "ca_key_file" -sha256 -days 365 -out "$ca_cert_file" \
  -subj '/CN=TLS Tutorial CA/C=DE/ST=Bavaria/L=Bavaria/O=Tutorial Organization'
```

For the sake of completeness it should be mentioned here that out in the wild server certificates are usually signed by subordinate CAs, so the client has to follow the certificate chain down all the way to a CA it trusts. Servers can even present server certificates that are combined with some intermediate CA certificates to fill the gaps. Even then the fact still remains, that client must know and trust the last CA in the chain.

We keep things simple here though since this is just a tutorial meant to convey the general idea of server certificate validation.