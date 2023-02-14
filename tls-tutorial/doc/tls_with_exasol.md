# TLS With Exasol

We covered the [basics of Transport Layer Security (TLS)](tls_introduction.md) in the first part of this series. We’ll now look at how you can use TLS with our analytics database and will focus on two areas:

1. Incoming TLS connections to Exasol services like JDBC, BucketFS or EXAOperation 
2. Outgoing connections where Exasol consumes services provided by a third party

## UDFs are Different

In a later article in this series we will take a close look at [using TLS inside User Defined Functions (UDFs)](tls_in_udfs.md). For now, all you need to know is that UDFs run in a sandbox that prevents them from using seeing and using files from the host operating system. While that makes UDFs more secure, it unfortunately means extra installation and configuration effort for users. We will only briefly touch on UDFs here. Please read the in-depth article on [TLS in UDFs](tls_in_udfs.md) for more details.

## Incoming TLS Connections

[EXAOperation](https://docs.exasol.com/db/latest/administration/on-premise/admin_interface/exaoperation.htm) and [BucketFS](https://docs.exasol.com/db/latest/database_concepts/bucketfs/bucketfs.htm) are probably the first touchpoints users have with Exasol. EXAOperation runs on HTTPS (i.e. HTTP + TLS) by default and BucketFS supports both HTTP and HTTPS. And, of course, HTTPS is the only suitable choice unless you’re running development tests.

This diagram shows two incoming TLS connections:

1. JDBC connection 
2. Web browser connecting to EXAOperation   

![Incoming TLS Connection](images/depl_incoming_connection.svg)

### The Default TLS Certificate and why you Should Replace it

In order to allow incoming connections from the start, Exasol comes preloaded with a default server certificate. Once you open a browser connection to that machine, your browser will display a warning that the connection isn’t secure. There are two reasons for this:

1. The certificate is signed by an Exasol Certification Agency (CA) and your machine does not have that CA’s certificate pre-installed 
2. Exasol can’t predict where the certificate is going to be hosted, so the hostname is left blank in the certificate

### The Chicken-or-the-egg Problem of Secure Connections

For a truly secure connection, you need to put in place a custom server certificate immediately after installing EXAOperation. Until then, you’re facing a classic chicken-or-the-egg problem. On the one hand, you need to install your own certificate to establish a trusted TLS connection. On the other, you need to connect to Exasol first in order to do this.

This graphic shows an example of a simple on-premise installation in a single network. The organization’s CA issues a root certificate. IT administrators then place this certificate on all users’ machines in that organization. The same administrators also install the server certificate via EXAOperation.

![Installing a server certificate](images/seq_installing_a_server_certificate.svg)

### Exasol TLS Certificate Installation (On-premise)

You can be reasonably sure with on-premise that it takes a matter of minutes to replace the certificate – and that no one will interfere.

However, if you plan to expose one or more of Exasol’s services to the internet, you need to install a proper certificate first and then open your firewall.

Another way to circumvent the problem is to use [Secure Shell (SSH)](https://en.wikipedia.org/wiki/Secure_Shell) to add the server certificates. Cloud providers allow you to install your own SSH keys directly with the installation of virtual machines. This gives you a secure starting point for the TLS certificate setup.

## Outgoing Connections

Depending on the certificate issuer outgoing TLS connections can either be as easy as pie or a right royal pain.

### Services With Certificates Issued by a Popular CA

If an application on your Exasol cluster wants to connect to the public application programming interface (API) of a service with a certificate signed by a popular CA – like one of those that come preloaded in most truststores – there isn’t much to do. The TLS connection should work out of the box. The TLS client can verify the server certificate by following the certificate chain down to that known root CA.

### Services With Certificates Issued by a Custom CA

This is where you need to work a bit harder to get TLS running. There are multiple reasons:

1. If you run an Exasol cluster, all data nodes must know the custom root CA &mdash; the certificate must be installed on all nodes 
2. [User Defined Functions (UDFs) can’t access the standard truststore](tls_in_udfs.md) as they run in an environment that only gives them a very restricted file system access 
3. The truststores the UDF container sees by default are installed with the language containers that means that getting in different certificates means either building custom language containers or completely overriding the truststore

### Different Clients, Different Truststores

To complicate things further, different clients tend to have their own truststores.

A Linux machine has a central truststore in the `/etc` directory, typically in form of text files. Java has its own truststore and file format. At least web browsers now use the truststore that comes with the operating system.

### The Use-case Dictates the Installation

Let's look at a few typical scenarios and what you need to get them working.

#### Data Import Using the EXALoader

If all you need is running [`IMPORT`](https://docs.exasol.com/db/latest/sql/import.htm) statements, certificate installation is still pretty straight forward.

Most of the connections that you will import from are JDBC connections and in that case you only need to install the necessary certificates in the Java truststore under `/etc/pki/ca-trust/extracted/java/cacerts` using Java's [keytool](https://docs.oracle.com/en/java/javase/11/tools/keytool.html).

##### Cluster Nodes and Truststores

Exasol distributes work across cluster nodes. While that is what you want of a performance database, it also means that the import can and will run on any of the data nodes of the cluster, so installing the certificates properly for an import means you need to install them on **all** cluster nodes.

There are several ways to achieve this.

* If you only have a few nodes in the cluster you could do it by hand.
* You could use [Distributed Shell (DSH)](https://manpages.org/dsh) to enter the command only once.
* With a larger cluster tools like [Ansible](https://www.ansible.com/), [Chef](https://www.chef.io/) or [Puppet](https://puppet.com/) can help you script the job.
* Or you could simply write a shell script and loop through the hosts.

We will not look at those options here in detail, because that would be way out of the scope of this article.

#### Using TLS in User Defined Functions (UDFs)

UDFs run in a sandbox for security reasons. The main effect you should be aware of in the context of TLS is that this means UDFs cannot see the host filesystem. And that means that the certificates on the host filesystem are visible to the EXALoader, but **not** to UDFs.

Since this is a tricky topic, we have a separate article about using [TLS in UDFs](tls_in_udfs.md).

### Combining UDFs With the EXALoader

This is the point where things get really complicated. As mentioned before, UDFs run in a sandbox. So they cannot see the certificate files on the host filesystem. The EXALoader can though.

In a scenario where you need both &mdash; let's say you want to use a Virtual Schema &mdash; you have different options.

Remember that if UDF and EXALoader only need to connect to servers that use a TLS certificate signed by a CA that is in the default trust stores, you don't have to do anything.

If on the other hand you want to use your own CA, we recommend that you install the certificates twice. In the host filesystem of each data node, so that the EXALoader can see it as described in section ["Data Import Using the EXALoader"](#data-import-using-the-exaloader). And additionally in BucketFS as described in ["TLS with UDFs"](tls_in_udfs.md).

There are other options like using symlinks from Buckets to the host file system to avoid duplication. But this has other side effects, so the cost-benefit ratio of installing the certificates twice is better.

## Conclusion

TLS with Exasol is simple as long as the CA certificates required are shipped with Exasol (and the language containers).

If you want to use certificates singed by you own CA for an Exasol cluster, you need to install those certificates. [UDFs run in a container, so special effort is needed to install certificates for them](tls_in_udfs.md).
