# vehicle-java17-sb3
A repo which I use for instructing Java 17 and Spring Boot 3

## Running the application
Before running the application, make sure to do a Maven ```clean verify```. After a successful build, run the application by hitting the play button in IntelliJ from the VehicleApplication.java file.

When the application is running, API documentation is available at [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html).

The UI for the in-memory H2 database is available at: [http://localhost:8080/h2-console](http://localhost:8080/h2-console). Make sure you change the JDBC url to whatever was logged in the startup logging of the application, for example:

```
H2 console available at '/h2-console'. Database available at 'jdbc:h2:mem:a914a71d-3978-4377-a4de-8c34825d9f35'
```

Please note that this JDBC url changes each time you (re)start the application. The user name is already filled out (SA), no password is required.


## Topics that are covered in the workshop
* Creating Java Records
* Looking at Java's Exception Framework, creating a custom RuntimeException
* Spring Beans and Dependency Injection in Spring, using different types of stereotypes (```@Component```, ```@Service```, ```@Controller```, ```@Repository```)
* Test slicing targeting different layers of our application (using ```@WebMvcTest``` for the controller, ```@DataJpaTest``` for the repository, ```@SpringJUnitConfig``` for any other bean)
* Generating mapping methods using MapStruct
* Creating entities and custom methods using Hibernate
* Creating database migrations using FlyWay
* Using the ```@Transactional``` annotation
* Using Mockito, AssertJ and JUnit
* Adding configuration to a Spring Boot project
* Adding dependencies and configuration to the ```pom.xml```
* Creating exception handlers for our Controller, using ```@RestControllerAdvice``` and ```@ExceptionHandler```
* Generating a Swagger page
* Using an H2 database for testing and running your application locally
