# Usage example

Once built, the JAR will be located at target/client.jar. These examples will use said path.

## Commands

### Help

The CLI includes a help option, which shows commands:

```
java -jar target/client.jar -h
```

This extends to the commands, showing arguments and options:

```
java -jar target/client.jar message -h
```

### Single Message

To send the message 'Hello' to localhost:8080:

```
java -jar target/client.jar message localhost 8080 Hello
```

### Empty Message

To send an empty TCP request to localhost:8080:

```
java -jar target/client.jar empty localhost 8080
```

### Multiple Messages

To send several consecutive messages to localhost:8080:

```
java -jar target/client.jar multiple localhost 8080
```
