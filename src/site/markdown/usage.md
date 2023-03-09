# Usage example

Once built, the JAR will be located at target/client.jar. These examples will use said path.

## Commands

This includes details on all the messages sent or received.

### Single Message

To send the message 'Hello' to localhost:8080:

```
java -jar target/client.jar message --host=localhost --port=8080 --message=Hello
```

### Empty Message

To send an empty TCP request to localhost:8080:

```
java -jar target/client.jar empty --host=localhost --port=8080
```

### Multiple Messages

To send several consecutive messages to localhost:8080:

```
java -jar target/client.jar multiple --host=localhost --port=8080
```

## Help

The CLI includes a help option, which shows commands:

```
java -jar target/client.jar -h
```

This extends to the commands, showing arguments and options:

```
java -jar target/client.jar message -h
```

## Debug

All the commands have a debug option, which prints logs on console:

```
java -jar target/client.jar message --host=localhost --port=8080 --message=Hello --debug
```
