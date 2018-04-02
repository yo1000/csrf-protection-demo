# CSRF Protection Demo

for stateless

```console
$ ./mvnw spring-boot:run -pl demo-server &
$ ./mvnw spring-boot:run -pl demo-client-allow &
$ ./mvnw spring-boot:run -pl demo-client-deny &
```

Browse to following URLs.

- [http://localhost:8081/csrf/demo](http://localhost:8081/csrf/demo) (demo-server)
- [http://localhost:8082/csrf/demo](http://localhost:8082/csrf/demo) (demo-client-allow)
- [http://localhost:8083/csrf/demo](http://localhost:8083/csrf/demo) (demo-client-deny)

Open Browser Developer tools.

Click to [Protect with Cookie & Header], [Protect], [Ignore]
