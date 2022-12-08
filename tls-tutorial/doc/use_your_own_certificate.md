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

1. create a directory where we will keep the files that we are creating in the course of this project.

    ```shell
    tutorial_dir="$HOME/tutorials/tls_with_exasol/import"
    mkdir -p "$tutorial_dir"
    cd "$tutorial_dir"
    ```



## Creating the Certificates

To get that right out of the way, yes, in a normal organization, you already have a Certification Agency (CA). But for the purpose of this tutorial, let's pretend you don't.

That means we will create our own, and that in turn gives us the opportunity to create the server certificates we are going to use quickly.

The script below takes you through the following steps:

1. Create a key pair for the CA
    ```shell
    ca_key_file="tls_tutorial_ca.key"
    openssl genrsa -aes256 -out "$ca_key_file" 4096
    ```
2. Use the private CA key to sign a CA certificate
3. Create a server certificate for the MySQL server
4. Sign the server certificate with the CA

```shell
ca_cert_file="tls_tutorial_ca.crt"
openssl req -x509 -new -nodes -key "ca_key_file" -sha256 -days 365 -out "$ca_cert_file" -subj '/CN=TLS Tutorial CA/C=DE/ST=Bavaria/L=Bavaria/O=Tutorial Organization'
```