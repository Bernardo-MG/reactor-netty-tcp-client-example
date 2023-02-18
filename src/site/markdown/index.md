# Reactor Netty TCP Client Example

A small Netty TCP client to serve as an example.

To use the project first package it:

```
mvn clean package
```

The JAR will be a runnable Java file. It can be executed like this:

```
java -jar target/client.jar message localhost 8080 Hello
```

To show other commands:

```
java -jar target/client.jar -h
```

You can use this project along these others:
- [Netty TCP Server Example](https://github.com/Bernardo-MG/netty-tcp-server-example)
- [Reactor Netty TCP Server Example](https://github.com/Bernardo-MG/reactor-netty-tcp-server-example)
