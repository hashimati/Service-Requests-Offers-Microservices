# Service Requests-Offers Microservices

a simple Microservice using Micronaut framework. The archeticture covers the follwing areas:
1. Service Discovary using Netflix Eureka or Consul.
2. Integration with Zuul Gateway.
3. Securing Microservices with JWT.
4. Using Micronaut Data with MySQL and Liquibase.
5. Using Reactive Mongodb.



# Requirements Description (Story)
  
  John wants to do a full maintenace for his apartment. He heared about an application Called "Request-Offer" app which will help him to find a maintenance with a good offer. John sends a request to "Request-Offer" service. On other hand, Mike is service provider. Mike sees John's offer. Mike sends a good offer to "Request-Offer" service with competitive price to meet John's request. John accepts Mike's offer among alot of other offers.

# Domains: 
The services have 3 entities:

1. User: This entity represents the application users  user should be either requester and provider.
2. Request: This entity represents the service request objects.
3. Offer: This entity represents the offers objects.

# Solution
The solution consists of 5 services 
1. [Eureka Server](https://start.spring.io/#!type=gradle-project&language=java&platformVersion=2.2.0.RELEASE&packaging=jar&jvmVersion=1.8&groupId=io.hashimati&artifactId=eureka&name=eureka&description=Demo%20project%20for%20Spring%20Boot&packageName=io.hashimati.eureka&dependencies=cloud-eureka) or [Consul](https://www.consul.io/downloads.html) Server: Service Discovery Server. 
2. [UsersService](https://www.microstarter.io/?g=io.hashimati&artifact=UsersService&build=Gradle&language=Java&profile=service&port=8888&javaVersion=8&d=security-jwt,discovery-eureka,liquibase): It is an account management services. It provides registration, authentication and authorization services. Also, it propagate JWT tokens to RequestsService and OffersService. UserService stores users' data in MySQL instance and uses Micronaut Data Framework to handle the data. 
3.  [RequestsService](https://www.microstarter.io/?g=io.hashimati&artifact=RequestsService&build=Gradle&language=Java&profile=service&port=-1&javaVersion=8&d=discovery-eureka,security-jwt,mongo-reactive): This service produces all services which are related to requests. The service stores the requests in MongoDB instance. Also, it will consume the offers services as required.  
4. [OffersService](https://www.microstarter.io/?g=io.hashimati&artifact=OffersService&build=Gradle&language=Java&profile=service&port=-1&javaVersion=8&d=discovery-eureka,security-jwt,mongo-reactive): This service produces all services which are related to offers. Offers objects will be stored in a MongoDB instance. OffersService invokes services from RequestsServices as required. 
5. [Gateway](https://start.spring.io/#!type=gradle-project&language=java&platformVersion=2.2.0.RELEASE&packaging=jar&jvmVersion=1.8&groupId=io.hashimati&artifactId=gateway&name=gateway&description=Demo%20project%20for%20Spring%20Boot&packageName=io.hashimati.gateway&dependencies=cloud-zuul,oauth2-resource-server,cloud-eureka,cloud-starter-consul-discovery,thymeleaf):  a Netfix Zuul gateway service. 

# Archeticture: 

![Image of Diagram](https://github.com/hashimati/Service-Requests-Offers-Microservices/raw/master/requests_offers_services.png)

# Step 1: Service Discovery Server 

# Step 2: Users Service 
Users Service is user management services. The service will provide basiclly registration, authentication and authorization functions. The service will handle user object and store them in MySQL instance. User POJO has three attributes of string data type which are username, password, and roles. The roles are represented as string delimated by commas. The service will use Micronaut Data API to handle CRUD operations. So, let's define the User POJO: 
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
The User Pojo is mapped to a table in the database. The is defined by the below statement. 
```sql
create table users (
    id BIGINT not NULL auto_increment,
    username varchar(25) not null,
    password varchar(15) not null,
    roles varchar(200) not null, 
    constraint username unique (username),
    constraint id_num unique (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```
We will use Liquibase to create the Users table once the UsersService is launched. This process is called database migration. The migration process could include table creation and data migration. Liquibase uses XML files to manipulate database migration. In this example, we will do only table creation in three steps. The first step is to define create users schema in XML file: 
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

Now, lets create User repository. As prerequisite, add Micronaut Data dependcies for JDBC and MySQL dependencies
```gradle
	annotationProcessor 'io.micronaut.data:micronaut-data-processor:1.0.0.M4'
	runtime 'io.micronaut.configuration:micronaut-jdbc-hikari'
	compile 'io.micronaut.data:micronaut-data-jdbc:1.0.0.M4'
	compile group: 'mysql', name: 'mysql-connector-java', version: '8.0.17'
	compileOnly 'jakarta.persistence:jakarta.persistence-api:2.2.2'
	runtime "io.micronaut.configuration:micronaut-jdbc-tomcat"
```

# Step 3: Requests Service 

# Step 4: Offers Service
# Step 5 Gateway

# Running Application
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


