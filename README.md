[![Build&Deploy](https://github.com/alinaberlin/e-commerce-shop/actions/workflows/maven-publish.yml/badge.svg)](https://github.com/alinaberlin/e-commerce-shop/actions/workflows/maven-publish.yml)
# Ecommerce Shop
## Intro
This project offers a simple, RESTFul implementation of an online shop using Spring Boot.
## Technologies
The following technologies are used in this project:
* Spring Boot
* Spring Rest
* Spring Security with JWT
* Spring Data JPA
* MySQL database
* Maven
## Project Structure and architecture
Using the following layered architecture, this project is set up as a traditional web project:
* Presentation Layer which it is implemented using Spring Rest
* Business Layer represented by a collection of Spring Beans marked with @Service to denote that they are part of the business layer
* Data Access Layer represented by a collection of Spring JPA Repositories

Each of this layers implementations can be found in their own Java packages:
* Presentation Layer in controllers package
* Business Layers in services package
* Data Access Layer is repositories package

Apart from the 3 layers, the project contains also addional classes that are necessary for this project to be complete and in way or an other are part of one of the 3 layers:
* Configuration: can be found in config package and containes one configuration bean, an http filter and the global exceptions handler
* Custom exceptions: are implemented in exceptions package
* Database models: are implementeds in models package and are part of DAL layer
* DTO's: are implemented in payloads package and are part of PL
## Testing
The project contains unit and integration tests, but taking into the nature of the project with a very simple business logic, integration are prefered.
In order to have a testing environment as similar as possible with our GCP deployment, Testcontainers framework is used in order to execute the tests against a MySQL database.
For testing the RESTFful end-points I am using REST-Assured framework.
## CI/CD
The CI/CD consist of a single pipeline implemented using GitHub Actions.
The pipeline is having a single job and several jobs.
On main branch all steps are executed, but on dev and feature branches only maven build&test step is executed.
The CI/CD pipeline running the build and the tests on every push and on every push on main branch it is executing a new deployment on GCP.
## How to run the project locally
In oder to run the project locally you need to install the following:
* OpenJdk 17
* Docker
* A MySQL database
  
How to run it:
* Build and test: ```./mvnw build```
* Build, test and package: ```./mvnw -B package```
* Start the app locally: ```./mvnw spring-boot:run```


