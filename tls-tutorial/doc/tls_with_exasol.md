# TLS With Exasol

We covered the [basics of Transport Layer Security (TLS)](tls_introduction.md) in the first part of this series. We’ll now look at how you can use TLS with our analytics database and will focus on two areas:

1. Incoming TLS connections to Exasol services like JDBC, BucketFS or EXAOperation 
2. Outgoing connections where Exasol consumes services provided by a third party

## UDFs are Different

In a later article in this series we will take a close look at [using TLS inside User Defined Functions (UDFs)](tls_in_udfs.md). For now, all you need to know is that UDFs run in a sandbox that prevents them from using seeing and using files from the host operating system. While that makes UDFs more secure, it unfortunately means extra installation and configuration effort for users. We will only briefly touch on UDFs here. Please read the in-depth article on [TLS in UDFs](tls_in_udfs.md) for more details.

## Incoming TLS Connections

[EXAOperation](https://docs.exasol.com/db/latest/administration/on-premise/admin_interface/exaoperation.htm) and [BucketFS](https://docs.exasol.com/db/latest/database_concepts/bucketfs/bucketfs.htm) are probably the first touchpoints users have with Exasol. EXAOperation runs on HTTPS (i.e. HTTP + TLS) by default and BucketFS supports both HTTP and HTTPS. And, of course, HTTPS is the only suitable choice unless you’re running development tests.

This diagram shows an incoming TLS connection with EXAOperation as the service and the user’s web browser as the consumer.

TODO: add incoming TLS diagram

### The Default TLS Certificate and why you Should Replace it

In order to allow incoming connections from the start, Exasol comes pre-loaded with a default server certificate. Once you open a browser connection to that machine, your browser will display a warning that the connection isn’t secure. There are two reasons for this:

1. The certificate is signed by an Exasol Certification Agency (CA) and your machine does not have that CA’s certificate pre-installed 
2. Exasol can’t predict where the certificate is going to be hosted, so the hostname is left blank in the certificate

### The Chicken-or-the-egg Problem of Secure Connections

For a truly secure connection, you need to put in place a custom certificate immediately after installing EXAOperation. Until then, you’re facing a classic chicken-or-the-egg problem. On the one hand, you need to install your own certificate to establish a trusted TLS connection. On the other, you need to connect to Exasol first in order to do this.

This graphic shows an example of a simple on-premise installation in a single network. The organization’s CA issues a root certificate. IT administrators then place this certificate on all users’ machines in that organization. The same administrators also install the server certificate via EXAOperation.

TODO: add certificate installation diagram 

### Exasol TLS Certificate Installation (On-premise)

You can be reasonably sure with on-premise that it takes a matter of minutes to replace the certificate – and that no one will interfere.

However, if you plan to expose one or more of Exasol’s services to the internet, you need to install a proper certificate first and then open your firewall.

Another way to circumvent the problem is to use [Secure Shell (SSH)](https://en.wikipedia.org/wiki/Secure_Shell) to add the server certificates. Cloud providers allow you to install your own SSH keys directly with the installation of virtual machines. This gives you a secure starting point for the TLS certificate setup.

## Outgoing Connections

Depending on the certificate issuer outgoing TLS connections can either be as easy as pie or a right royal pain.

### Services With Certificates Issued by a Popular CA

If an application on your Exasol cluster wants to connect to the public application programming interface (API) of a service with a certificate signed by a popular CA – like one of those that come pre-loaded in most truststores – there isn’t much to do. The TLS connection should work out of the box. The TLS client can verify the server certificate by following the certificate chain down to that known root CA.

### Services With Certificates Issued by a Custom CA

This is where you need to work a bit harder to get TLS running. There are multiple reasons:

1. If you run an Exasol cluster, all data nodes must know the custom root CA &mdash; the certificate must be installed on all nodes 
2. [User Defined Functions (UDFs) can’t access the standard truststore](tls_in_udfs.md) as they run in an environment that only gives them a very restricted file system access 
3. The truststores the UDF container sees by default are installed with the language containers that means that getting in different certificates means either building custom language containers or completely overriding the truststore

### Different Clients, Different Truststores

To complicate things further, different clients tend to have their own truststores.

A Linux machine has a central truststore in the `/etc` directory, typically in form of text files. Java has its own truststore and file format. So does Python. At least web browsers now use the truststore that comes with the operating system.

### The Use-case Dictates the Installation

Let's look at a few typical scenarios and what you need to get them working.

### Data Import Using the EXALoader

If all you need is running [`IMPORT`](https://docs.exasol.com/db/latest/sql/import.htm) statements, certificate installation is still pretty straight forward.

Most of the connections that you will import from are JDBC connections and in that case you only need to install the necessary certificates in the Java truststore under `/etc/pki/ca-trust/extracted/java/cacerts` using Java's [keytool](https://docs.oracle.com/en/java/javase/11/tools/keytool.html).

### Cluster Nodes and Truststores

Exasol distributes work across cluster nodes. While that is what you want of a performance database, it also means that the import can and will run on any of the data nodes of the cluster, so installing the certificates properly for an import means you need to install them on **all** cluster nodes.  

### Installing CA Certificates on an Exasol Cluster

The previous graphic shows the effort needed to install CA certificates for all languages on all cluster nodes. On the plus side most times you only really need to place the CA certificate in the Java truststore under: `/etc/pki`

Installation in other truststores is optional and rarely needed. You’re most likely going to use the outgoing connection from the [EXALoader](https://docs.exasol.com/db/latest/planning/data_migration.htm#ExaLoaderImportCommand) to run [`IMPORT`](https://docs.exasol.com/db/latest/sql/import.htm) statements. The Loader part that connects to external JDBC databases is written in Java, so extending the central Java truststore is sufficient to get `IMPORT` working with TLS all cases that use a JDBC connection.

### Installing new TLS Certificates on an Exasol Cluster

Before you start installing TLS certificates, you need to ask yourself:

1. Do I need TLS connections from UDFs or only from the EXALoader (i.e. for the `IMPORT` command)? 
2. If I need UDFs, what programming languages do I need to support?

Installing certificates so that the EXALoader can use them for imports is fairly straightforward. If the external source should be accessed via JDBC, you need to add your certificates via Java’s keytool on all data nodes in the cluster. The Java truststore is located under

    /etc/pki/ca-trust/extracted/java/cacerts

### 

The trouble starts when you need TLS in a UDF. The UDF truststores currently form an integral part of the language containers. They can and should be modified in situ. That way client applications work without modification or special configuration because the truststore is in its default location inside the running container. But there are also downsides. Firstly, this only applies until a language container update. And secondly, you have to locate the truststore files first – and they have variable path elements unlike the central one.

To find the Java truststore in the language container, run the following command:

updatedblocate --regex 'ScriptLanguages.*/java/cacerts'

Remember, you have to install the certificates on all data nodes.
Reinstalling the certificates after a language container update

Updates of a language container currently overwrite the corresponding truststores. So you need to remember to reinstall all custom certificates after any update.
The future of TLS in UDF containers: shared truststores

To improve handling of TLS certificates in UDF containers, we’re planning to share truststores from the base system with the UDFs. This means you can install certificates in the central truststore and they’ll be available to UDFs too – but not the other way round to prevent security issues.