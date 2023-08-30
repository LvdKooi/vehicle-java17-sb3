# vehicle-java17-sb3
A repo which I use to teach Java 17 and Spring Boot 3

## Running the application
Run the application by hitting the play button in the VehicleApplication.java file from IntelliJ.

When the application is running, API documentation is available at [http://localhost:8080/swagger-ui/index.html]().

The UI for the in-memory H2 database is available at: [http://localhost:8080/h2-console](). Make sure you change the JDBC url to whatever was logged in the startup logging of the application, for example:

```
H2 console available at '/h2-console'. Database available at 'jdbc:h2:mem:a914a71d-3978-4377-a4de-8c34825d9f35'
```

Please note that this JDBC url changes each time you (re)start the application. The user name is already filled out (SA), no password is required.
