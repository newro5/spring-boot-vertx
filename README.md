# spring-boot-vertx
playing around with integrating spring-boot and vertx 

##Build and run
1. Perform a mvn package
2. Run the rest-service-1.0-SNAPSHOT.jar

##Demonstrable APIs
1. Spring GreetingController - http://localhost:8080/greeting?name=John
2. Vertx GreetingVerticle - http://localhost:9090/greeting?name=John

##What this demonstrates
SpringBoot instantiating and configuring Vertx verticles
