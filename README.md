# Service Requests-Offers Microservices


What Is Micronaut?
In a nutshell, Micronaut is a lightweight, JVM-based framework that's ahead of compilation time, with less startup time.


Service Requess-Offers Microservices is a simple microservices application using Micronaut framework. The application covers the following areas:
1. Service Discovary using Netflix Eureka or Consul.
2. Integration with Zuul Gateway.
3. Securing Microservices with JWT.
4. Using Micronaut Data with MySQL and Liquibase.
5. Using Reactive Mongodb.



## Requirements Description (Story)
  
  John wants to do a full maintenace for his apartment. He heared about an application Called "Request-Offer" app which will help him to find a maintenance with a good offer. John sends a request to "Request-Offer" service. On other hand, Mike is service provider. Mike sees John's offer. Mike sends a good offer to "Request-Offer" service with competitive price to meet John's request. John accepts Mike's offer among alot of other offers.

## Domains: 
The services have 3 entities:

1. User: This entity represents the application users  user should be either requester and provider.
2. Request: This entity represents the service request objects.
3. Offer: This entity represents the offers objects.

## Solution
The solution consists of 5 services 
1. [Eureka Server](https://start.spring.io/#!type=gradle-project&language=java&platformVersion=2.2.0.RELEASE&packaging=jar&jvmVersion=1.8&groupId=io.hashimati&artifactId=eureka&name=eureka&description=Demo%20project%20for%20Spring%20Boot&packageName=io.hashimati.eureka&dependencies=cloud-eureka) or [Consul](https://www.consul.io/downloads.html) Server: Service Discovery Server. 
2. [UsersService](https://www.microstarter.io/?g=io.hashimati&artifact=UsersService&build=Gradle&language=Java&profile=service&port=8888&javaVersion=8&d=security-jwt,discovery-eureka,liquibase): It is an account management services. It provides registration, authentication and authorization services. Also, it propagate JWT tokens to RequestsService and OffersService. UserService stores users' data in MySQL instance and uses Micronaut Data Framework to handle the data. 
3.  [RequestsService](https://www.microstarter.io/?g=io.hashimati&artifact=RequestsService&build=Gradle&language=Java&profile=service&port=-1&javaVersion=8&d=discovery-eureka,security-jwt,mongo-reactive): This service produces all services which are related to requests. The service stores the requests in MongoDB instance. Also, it will consume the offers services as required.  
4. [OffersService](https://www.microstarter.io/?g=io.hashimati&artifact=OffersService&build=Gradle&language=Java&profile=service&port=-1&javaVersion=8&d=discovery-eureka,security-jwt,mongo-reactive): This service produces all services which are related to offers. Offers objects will be stored in a MongoDB instance. OffersService invokes services from RequestsServices as required. 
5. [Gateway](https://start.spring.io/#!type=gradle-project&language=java&platformVersion=2.2.0.RELEASE&packaging=jar&jvmVersion=1.8&groupId=io.hashimati&artifactId=gateway&name=gateway&description=Demo%20project%20for%20Spring%20Boot&packageName=io.hashimati.gateway&dependencies=cloud-zuul,oauth2-resource-server,cloud-eureka,cloud-starter-consul-discovery,thymeleaf):  a Netfix Zuul gateway service. 

## Archeticture: 

![Image of Diagram](https://github.com/hashimati/Service-Requests-Offers-Microservices/raw/master/requests_offers_services.png)

## Implementation
### Step 1: Service Discovery Server 
Service Discovery Server is a namiong service for the services. Each service will register itself in the service discovery services. Therefore, the services will contact with each others by their name. Also, provide a services to inquiry about the availability of each services instances. In our application you can use Eureka or Consul as a service discovery server. 

#### Netflix Eureka Service Discovery Server. 
Eureka Service Discovery server is spring boot application which is usually listening on port 8761. To configure it, open application.properties file  and add the below configurations.  
```
src\main\resources\application.properties
```
```properties
server.port=8761
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
```
Then, annotate the main class with @EnableEurekaServer annotation. By these two steps, you did the basic configuration for Eureka Servcie Discovery Server. 
```
src\main\java\io\hashimati\EurekaService\EurekaServiceApplication.java
```
```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class EurekaServiceApplication {
	public static void main(String[] args) {

	    SpringApplication.run(EurekaServiceApplication.class, args);
	}
}
```

#### Using Consul

Consul is a service discovery solution which is maintained by HashiCorp. You can use as a blackbox solution for discovery service. download the community edition. The create extract the executable file into your selected path. After that create a folder for servcie data in your file system in order to pass into running command. Consul service is listening by default on port 8500. To run Consul agent, use the below command: 
```shell
> consul agent -data-dir=your-consul-data-file -dev -ui
```

### Step 2: Users Service 
Users Service is a user management and JWT propagation service. The service will provide basiclly user registration, authentication and authorization functions. The service will handle user objects and store them into MySQL instance. User POJO has three attributes of string data type which are username, password, and roles. The roles are represented as a string delimated by commas. The service will use Micronaut Data API to handle CRUD operations. 

[UsersService](https://www.microstarter.io/?g=io.hashimati&artifact=UsersService&build=Gradle&language=Java&profile=service&port=8888&javaVersion=8&d=security-jwt,discovery-eureka,liquibase) is generated by running this Micronaut Cli command:

```shell
mn create-app io-hashimati-UsersService --profile service --lang java --build gradle --features security-jwt --features discovery-eureka --features liquibase 
```
#### Creating User POJO
As prerequisite, add Micronaut Data dependcies for JDBC and MySQL dependencies
```gradle
annotationProcessor 'io.micronaut.data:micronaut-data-processor:1.0.0.M4'
runtime 'io.micronaut.configuration:micronaut-jdbc-hikari'
compile 'io.micronaut.data:micronaut-data-jdbc:1.0.0.M4'
compile group: 'mysql', name: 'mysql-connector-java', version: '8.0.17'
compileOnly 'jakarta.persistence:jakarta.persistence-api:2.2.2'
runtime "io.micronaut.configuration:micronaut-jdbc-tomcat"
```

After adding the Micronaut Data dependencies, configure the database connection in application.yml file. The required configurations are 

| url | driverClassName | username | password | dialect |
| --- | --- | --- | --- | --- |
| jdbc:mysql://127.0.0.1:3306/helloworlddb | com.mysql.cj.jdbc.Driver | root | Hello@1234 | MYSQL |

```
src\main\resources\application.yml
```
```yml
datasources:
  users:
    url: jdbc:mysql://127.0.0.1:3306/helloworlddb
    driverClassName: com.mysql.cj.jdbc.Driver
    username: root
    password: 'Hello@1234'
    dialect: MYSQL
```
Now, let's define the User POJO: 
```
/src/java/main/io/hashimati/usersservices/domains
```
```java
@Entity
@Table(name="users")
public class User
{

    @Id
    @GeneratedValue
    private Long id;
    
    private String username;
    private String password;
    private String roles;
}
```
The User Pojo is mapped to a table in the database by using below statement. 
```sql
create table users (
    id BIGINT not NULL auto_increment,
    username varchar(25) not null,
    password varchar(200) not null,
    roles varchar(200) not null, 
    constraint username unique (username),
    constraint id_num unique (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```
The CREATE TABLE statement can be run manually in the database or we can use frameworks like Flyway or Liquibase to execute it when we launch the service. This process is called database migration. The migration process could include schema creation and data migration. One advantage of using Liquibase or Flyway frameworks is to keep tracks of our schemas versions and to log the changes in well organized and structured table that data migration fromeworks provide. In this application, we will use Liquibase to create the Users table once the UsersService is run. Liquibase will look into the definitions files and it will execute the defined DDL and SQL statements before exposing the services. Liquibase uses XML files to manipulate database migration. In this application, we will do only table creation in three steps. The first step is to define create users schema in XML file. The file name should be in the following format virsion-filename.xml. So, the file name should be 01-create-users-schema.xml where "01" prefix indicates to the version of Users Table. Whenever you update the schema save the changes in a new file and increment the version prefix.   
```
src\main\resources\db\changelog\01-create-users-schema.xml
```
```xml
<?xml version="1.0" encoding="UTF-8"?>

<databaseChangeLog
  xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog
         http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.1.xsd">
  <changeSet id="01" author="hashimati">
    <createTable tableName="users"
      remarks="A table to contain all users">
      <column name="id" type="int" autoIncrement="true">
        <constraints nullable="false" unique="true" primaryKey="true" />
      </column>
      <column name="username" type="varchar(25)">
        <constraints nullable="false" unique="true"/>
      </column>
      <column name="password" type="varchar(200)">
        <constraints nullable="false" unique="false"/>
      </column>
      
      <column name="roles" type="varchar(200)">
        <constraints nullable="false" unique="false"/>
      </column>
    </createTable>
  </changeSet>
</databaseChangeLog>
```
The second step, we will include Users schema creation in the liquibase changelog xml file. 
```
src\main\resources\db\liquibase-changelog.xml
```
```xml
<?xml version="1.0" encoding="UTF-8"?>
<databaseChangeLog
  xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog
         http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.1.xsd">
  <include file="changelog/01-create-users-schema.xml" relativeToChangelogFile="true"/>
</databaseChangeLog>
```
The thrid step is the configuration of the change log file in application.yml file. 
```
src\main\resources\application.yml
```
```yml
liquibase:
  datasources:
    users:
      change-log: 'classpath:db/liquibase-changelog.xml'
```

As mentioned in the requirement, the user could be Service requester as "user" or Service provider as "service_provider". So, we will declare these two roles as two constant attributes of type string under Roles.java class. The Roles Class will be replicated in Requests Service and Offers Services. 
```
src\main\java\io\hashimati\usersservices\constants\Roles.java
```
```java
public class Roles {
    public static String SERVICE_PROVIDER = "service_provider"; 
    public static String USER = "user"; 
    
}
```
To handle User CRUD, we will define UserRepository interface. The UserRepsoitory should extend CrudRepository interface and annotated with @JdbcRepository annotation. Because the service is connected to MySQL instance, we will pass Dialect.MYSQL into dilect attribute of the @JdbcRepository   
```
src\main\java\io\hashimati\usersservices\repository\UserRepository.java
```
```java
import io.hashimati.usersservices.domains.User; 
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

@JdbcRepository(dialect = Dialect.MYSQL)
public interface UserRepository extends CrudRepository<User, Long>
{
    public User findUserByUsername(String username);
    public boolean existsByUsername(String username); 
}
```
In UserRepository, we defined two funcitons: findUserByUsername(String username) to retreive a particular user object by username and existsByUsername(String username) function which returns true if the user exist in the database or false if the user is not exist in the database. 

#### Security Configuration
Now, we are ready to work with security configuration. Micronaut has simplified the implementation of JWT authentication and authorization. To implement security we need to create your AuthenticationProvider class and configure JWT properties in application.yml file. Before these two steps, we need to encrypt the user's password before storing it into the database. To acheive this goal, we will use BCryptPasswordEncoder of spring-security-crypto API to encrypt and match passwords. 
```build
// https://mvnrepository.com/artifact/org.springframework.security/spring-security-crypto
compile group: 'org.springframework.security', name: 'spring-security-crypto', version: '5.2.0.RELEASE'
```
In BCPasswordEncoder class, we will define the BCryptPasswordEncoder bean. @Factory and @Prototype annotations are equivelant to @Configuraiton and @Bean annotations in Spring Boot. 
```
src\main\java\io\hashimati\usersservices\security\BCPasswordEncoder.java
```
```java
@Factory
public class BCPasswordEncoder{    
    @Prototype
    public PasswordEncoder passwordEncoder(){

        return new BCryptPasswordEncoder();
    }
}
```
Now, we can write the Authentication Provider class. The authentication provider class implements AuthenticationProvider interface. The implemenation requires overriding authenticate() method in which the users' credentials are validated. 

```java
@Singleton
public class AuthenticationProviderUserPassword implements AuthenticationProvider  {

    @Inject
    private UserRepository userRepository;

    @Inject 
    private PasswordEncoder PasswordEncoder; 


    @Override
    public Publisher<AuthenticationResponse> authenticate(AuthenticationRequest authenticationRequest) {

        //if User is not exist, return Authentication Failed
        if(!userRepository.existsByUsername(authenticationRequest.getIdentity().toString())){
            
            return Flowable.just(new AuthenticationFailed(AuthenticationFailureReason.USER_NOT_FOUND)); 
        }

        User user = userRepository.findUserByUsername(authenticationRequest.getIdentity().toString());  
        
    
        if ( PasswordEncoder.matches(authenticationRequest.getSecret().toString(), user.getPassword())) {
            return Flowable.just(new UserDetails(user.getUsername(),
                    Arrays.asList(user.getRoles()
                            .replace(" ", "")
                            .split(","))));
        }
        
        return Flowable.just(new AuthenticationFailed(AuthenticationFailureReason.CREDENTIALS_DO_NOT_MATCH)); 
    }
}
```
Then, we will exponse the users registration via /signup/{role} POST Endpoint.{role} is a path variable which holds either "user" or "service_provider". Therefore, the user's role will determented based on {role} value. The SignUp() method is annotated with  @Secured(SecurityRule.IS_ANONYMOUS) which mean it does not require users to be authenticated to consume it. The login and oauth endpoints are built-in in Micronaut-JWT which means that you can configure them in the applicaiton.yml file.

```java
 @Controller("/")
public class UserController {

    @Inject
    UserRepository userRepository; 
    
    @Inject
    private PasswordEncoder passwordEncoder; 

    @Secured(SecurityRule.IS_ANONYMOUS)
    @Post("/signup/{role}")
    public Single<String> signUp(@Body User user, @PathVariable(value = "role") String role) 
    {
        if(role.equals(Roles.USER) || role.equals(Roles.SERVICE_PROVIDER))  
        {
            user.setRoles(role);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            try{
                if(!userRepository.existsByUsername(user.getUsername()))
                 {
                     userRepository.save(user); 
                     return Single.just("done!");
                 } 
                 else 
                 {
                     return Single.just("The user is already exist"); 
                 }
            }
            catch(Exception ex)
            {
                ex.printStackTrace();; 
                return Single.just("Something is going wrong! Please contact The administrators!"); 
            }
        }
        return Single.just("Invalid Request"); 
    }
}
```
Finally, we will configure security and JWT properties in application.yml. 
```
src\main\resources\application.yml
```
```yml
micronaut:
  security:
    enabled: true  #1
    endpoints:
      login:
        enabled: true #2
      oauth:
        enabled: true #3
    token:
      jwt:
        enabled: true  #4
        signatures:
          secret:
            generator:
              secret: pleaseChangeThisSecretForANewOne #5
      writer:
        header:
          enabled: true	#6
          headerName: "Authorization"  #7
          prefix: "Bearer " #8
      propagation:
        enabled: true #9
        service-id-regex: "offers-services|requests-services|gateway" #10
```
1) Enabling The security
2) To enable login endpoint
3) To enable oauth endpoint
4) to enable JWT token
5) to provide the secret in the configuration which the sevice uses to generate the token. 
6) To enable header. 
7) To specify the header name. 
8) To specify the prefix of the authentication. 
9) To enable propagation 
10) To provide the services ids to which the UsersService will propagate the JWT secret. 

#### Service Discovery Client Configuration: 
If you want to use Netfilx Eureka Discovery add Eureka configuration, add Eureka client configurations to application.yml file
```
src\main\resources\application.yml
```
```yml
 eureka:
   client:
     enabled: true
     registration:
       enabled: true
     defaultZone: "${EUREKA_HOST:localhost}:${EUREKA_PORT:8761}"
```
But if you prefer to use Consul, add Consul client configuraiton instead of Eureka client configurations. 
```yml
consul:
  client:
    registration:
      enabled: true
    defaultZone: "${CONSUL_HOST:localhost}:${CONSUL_PORT:8500}"
```
The Service Discovery client configurations are common in UsersService, RequestsService, and OffersService. 

### Step 3: Requests Service 
Requests Service is a Microservice which produces Requests related services. This services is Micronaut service. To generate [RequestsService](https://www.microstarter.io/?g=io.hashimati&artifact=RequestsService&build=Gradle&language=Java&profile=service&port=-1&javaVersion=8&d=discovery-eureka,security-jwt,mongo-reactive) run this Micronaut Cli command: 
```shell
> mn create-app io-hashimati-RequestsService --profile service --lang java --build gradle --features discovery-eureka --features security-jwt --features mongo-reactive 
```
#### Security Configuration 
The first step is to configure JWT security. The RequestService and OfferService are getting JWT propagation from UsersServices. So, the users cannot be authenticated by these two services. So, they will only validate the JWT token. So, we need to ensure the JWT validation configuration to application.yml of RequestsService and OffersService. 
```yml
micronaut:
  security:
    enabled: true
    token:
      jwt:
        enabled: true
        signatures:
          secret:
            validation: # to vlidate token. 
              secret: pleaseChangeThisSecretForANewOne
```

#### Requests Service Implemenation. 
In this service will define Service Request Pojo and Offer classes. These two classes will be defined in both Requests service and Offer services. Both requests and offers objects are stored in MongoDB instance. We will define the Request Pojo as follwoing


```java 
import java.util.Date;
import io.hashimati.requestservice.domains.enums.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Data
public class Request {
    private String id, type, title, detail, requesterName, city;

    private RequestStatus status = RequestStatus.INITIATED;
    private Date date , lastUpdate = date = new Date();

    private Location location; 
    
}
```
The Request object has three possible statuses: INITIATED, DONE, and CANCELED. The "location" attribute in the Request class will be used to by Service Provider users to find requests near to them using MongoDB Geospecial features. In this artical, we will not talk about this feature but you can review the implementation in the source code repository.

```
src\main\java\io\hashimati\requestservice\domains\enums\RequestStatus.java
```
```java
public enum RequestStatus {
    INITIATED, DONE, CANCELED ;
}
```
```
src\main\java\io\hashimati\requestservice\domains\Location.java
```
```java
import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString; 

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Location {

    private String type = "Point"; 
    
    private ArrayList<Double> coordinates = new ArrayList<Double>(); 

}

```

The Offer Pojo is define like this 
```java
import java.util.Date;
import io.hashimati.requestservice.domains.enums.OfferStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Data
public class Offer {
    private String id, by, message, orderNumber;
    private double price; 
    private OfferStatus status = OfferStatus.SENT;
    private Date date , lastUpdate = date = new Date();
}
```
Offer object should have one of the following statuses: SENT, REJECTED, ACCEPTED 
```java
public enum OfferStatus {
    ACCEPTED, REJECTED, SENT;
}
```
Based on Request class, we will implement two classes.The fiest one is RequestService to handle the requests object. The second class is RequestController which exposes the REST servies that are related to Requests objects. Before starting the implemenation of the RequestsServices class, we need to configure MongoDB instance's URI in application.yml file. The MongoDB instance is lestining on port 27017. These are the database information

| host | Port | Database Name | Request Collection | Offer Collection |
| --- | --- | --- | --- | --- |
| localhost | 27017 | requestsDB | request | Offer |



```
src\main\resources\application.yml
```
```yml
mongodb:
  uri: "mongodb://${MONGO_HOST:localhost}:${MONGO_PORT:27017}"
```

Now, we can start to write RequestService class. First, we will do the following steps: 
1) Inject mongoClient bean. 
2) Implement getCollection() function which returns "requests" collection. 
3) Implement findAsSingle() function which takes MongoDB query as BsonDocument object and returns Single<Request> object or nothing. This method will be used whenever we want to retreive on Request object.
4) Implement findAsSingle() function which takes MongoDB query as BsonDocument object and returns stream of Request objects. This method will be invoked whenever we want to retreive a stream of Request objects. 

```java 
@Singleton
public class RequestService {

    private final MongoClient mongoClient;

    public RequestServices(MongoClient mongoClient)
    {
        this.mongoClient = mongoClient;
    }


    private MongoCollection<Request> getCollection() {
            return mongoClient
                .getDatabase("requestsDB")
                .getCollection("requests", Request.class);
    }

    private Single<Request> findAsSingle(BsonDocument query)
    {

        return Single.fromPublisher(getCollection().find(query)); 
    }
    
    private Flowable<Request> findAsFlowable(BsonDocument query)
    {
    	return Flowable.fromPublisher(getCollection().find(query)); 
    }
    ...
}
```
In RequstService, save() function will store Request object into requests collection. Each request should have unique ID. So the easy way to create unique is use this format username_incrementalNo. So, whenver, whenever the method receives new Request object from as specific user, it will count the requests of that user. Next, it will add one to the counting result. Then, it will set the request ID based on that format and the request's status to INITIATED which mean that the system receives the request. The method basically will reteive Single<Request> object with new ID. 

```java
@Singleton
public class RequestService {
 ...
 public Single<Request> save(Request request){

        Long x = Single.fromPublisher(getCollection()
                .countDocuments(new BsonDocument()
                        .append("requesterName", new BsonString(request.getRequesterName()))))
                        .blockingGet();
        request.setId(request.getRequesterName() + "_" + x.longValue());
   
	request.setStatus(RequestStatus.INITIATED);
   
   	return   Single.fromPublisher(getCollection().insertOne(request))
                .map(success->request);

    }
...
}    
```
In RequestService, we define the below RequestObject retrieval functions which are using findAsSingle() and FindAsFlow() functions

| function | Description |
| --- | --- |
| findRequestByNo(String requestNo) | It retrieve a request by request number attribute |
| findAll() | It retreives all INITIATED requests in the system |
| findAll(String username) | It retreives all requests of the user |
| findByCity(String city) | to find the INITIATED requests in a specific city |
| findNearBy(location) | to find the INITIATED requests near by service provider. |
```java
@Singleton
public class RequestService {
 ...
    public Single<Request> findRequestByNo(String requestNo) {
        return findAsSingle(new BsonDocument().append("_id", new BsonString(requestNo))); 
    }
    
    public Flowable<Request> findAll()
    {
            BsonDocument query = new BsonDocument().append("status", new BsonString(RequestStatus.INITIATED.toString())); 
            return findAsFlowable(query); 
    }
    
    public Flowable<Request> findAll(String username){
        return findAsFlowable(new BsonDocument()
                                .append("requesterName", new BsonString(username))); 
    }
    
    public Flowable<Request> findByCity(String city) {
        BsonDocument query = new BsonDocument()
        .append("status", new BsonString(RequestStatus.INITIATED.toString()))
        .append("city", new BsonString(city)); 
	
        return findAsFlowable(query); 
    }
...
}  
```
In the RequestsService class, there is takeAction(String requestId, RequestStatus done) function. this function will be used to change status of the of the request. 

```java 
	public Single<String> takeAction(String requestId, RequestStatus done){
        	BsonDocument filter = new BsonDocument().append("_id", new BsonString(requestId)); 
        	Request request = findAsSingle(filter).blockingGet(); 
        	request.setStatus(done);
		
		return Single.fromPublisher(getCollection().findOneAndReplace(filter, request))
		.map(success->"success"
		.onErrorReturnItem("failed"); 
	}

```
Finally, all the function in the RequestService class will be exposed in RequestController class. The RequestController class is injected with two beans: 
1) RequestService: To expose RequestService's functions.
2) offersClient: offerClient will be explained in Step 5. 

```java
@Controller("/api")
public class RequestController {


    @Inject
    private RequestServices requestServices;

    @Inject
    private OffersClient offersClient; 

}
```


The Requests uses HttpClient intrerface to handle Offer object by consumeing the required services from OffersServices. You learn more about it in "Step 5: Interaction Between RequestsServcie and OffersService" Section. 


### Step 4: Offers Service
to be written
### Step 5: Interaction Between RequestsServcie and OffersService
### Step 6 Gateway
to be written
## Running Application
1. Ensure MySql and MongoDB instances are installed, configured and run. 

2. Create new database in the MySQL instance with name "helloworlddb": 
```sql
CREATE DATABASE helloworlddb
```

3. Start Discovry Server (Consul or Eureka):

To start Consul, run this command:
```shell
> consul agent -data-dir=your-consul-data-file -dev -ui
```
To start Eureka, run this command from Eureka directory: 
```shell
> gradlew bootRun
```

4. Run UsersService, RequestsService, and OffersService by run this command for each service: 
```shell
> gradlew run
```

5. Run Gateway :
```shell
> gradlew bootRun
```

## Conclusion
