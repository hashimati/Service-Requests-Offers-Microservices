# Service Requests-Offers Microservices

a simple Microservice using Micronaut framework. The archeticture covers the follwing areas:
1. Service Discovary Netflix Eureka or Consul.
2. Integration with Zuul Gateway.
3. Securing Microservices Mesh with JWT.
4. Using Micronaut Data with MySQL.
5. Using Reactive Mongodb.



# Requirements Description (Story)
Suppose John wants to do a full maintenace for his apartement. He heared about an application Called "Request-Offer" app which will help him to find a mentenance with a good offer. John sends a request to "Request-Offer" service. On other hand, Mika is service provider. Mike sees John's offer. Mike sends a good offer to "Request-Offer" service with competitive price to meet John's request. John accepts Mike's offer among alot of other offers.

# Domains: 
The services have 3 entities:

1. User: This entity represents the application users  user should be either requester and provider.
2. Request: This entity represents the service request objects.
3. Offer: This entity represents the offers objects.

# Solution
The solution consists of 5 services 
1. [Eureka Server](https://start.spring.io/#!type=gradle-project&language=java&platformVersion=2.2.0.RELEASE&packaging=jar&jvmVersion=1.8&groupId=io.hashimati&artifactId=eureka&name=eureka&description=Demo%20project%20for%20Spring%20Boot&packageName=io.hashimati.eureka&dependencies=cloud-eureka) or Consul Server: Service Discovery Server. 
2. [UsersService](https://www.microstarter.io/?g=io.hashimati&artifact=UsersService&build=Gradle&language=Java&profile=service&port=8888&javaVersion=8&d=security-jwt,discovery-eureka): It is an account management services. It provides registration, authentication and authorization services. Also, it propagate JWT tokens to OrdersService and OffersService. UserService stores users' data in MySQL instance and uses Micronaut Data Framework to handle the data. 
3.  OrdersService: This service produces all services which are related to orders. The service stores the orders in MongoDB instance. Also, it will consume the offers services as required.  
4. OffersService: This service produces all services which are related to offers. Offers objects will stores. OffersService invokes services from OrdersService as required. 
5. [Gateway](https://start.spring.io/#!type=gradle-project&language=java&platformVersion=2.2.0.RELEASE&packaging=jar&jvmVersion=1.8&groupId=io.hashimati&artifactId=gateway&name=gateway&description=Demo%20project%20for%20Spring%20Boot&packageName=io.hashimati.gateway&dependencies=cloud-zuul,oauth2-resource-server,cloud-eureka,cloud-starter-consul-discovery,thymeleaf):  a Netfix Zuul gateway service. 

# Archeticture: 

![Image of Diagram](https://github.com/hashimati/Service-Requests-Offers-Microservices/raw/master/requests_offers_services.png)
