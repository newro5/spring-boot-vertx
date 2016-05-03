# spring-boot-vertx
Playing around with integrating spring-boot and vertx 

##Build
Run a mvn package build. Use the included settings.xml if you don't have one locally.

##Run
Run the spring-boot-vertx-1.0-SNAPSHOT.jar executable jar

##Demonstrable APIs
1. Spring GreetingController - http://localhost:8080/greeting?name=John
2. Vertx GreetingVerticle - http://localhost:9090/greeting?name=John

##What this demonstrates
SpringBoot instantiating and configuring Vertx verticles
