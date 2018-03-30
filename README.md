# CSRF Protection Demo

for stateless

```console
$ ./mvnw spring-boot:run -pl csrf-protection-demo-server &
$ ./mvnw spring-boot:run -pl csrf-protection-demo-client &
```

Browse to [http://localhost:8082/csrf/demo](http://localhost:8082/csrf/demo)

Open Browser Developer tools.

Click to [Protect with Cookie & Header], [Protect], [Ignore]
