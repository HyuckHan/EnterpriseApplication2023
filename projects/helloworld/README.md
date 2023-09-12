# HelloWorld

## Compilation 

### Compile the proto file

```
$ ant proto_compile
```

### Compile stub files

```
$ ant stub_compile
```

### Compile client srcs

```
$ ant client_compile
```

### Compile server srcs

```
$ ant server_compile
```

### Compile all codes

```
$ ant 
```

## Run

### Run the server
```
$ java -cp java -cp "build/mini-project.jar:../lib/*" HelloWorldServer
```

### Run the client
```
$ java -cp java -cp "build/mini-project.jar:../lib/*" HelloWorldClient
```
