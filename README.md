# Application

This is a simple web application to manage leasing contracts in Allane.
This application has the following tech stack:
1. Java 11 with Spring Boot 2.7.5
2. Angular 14
3. MysQL 8

Additional notes:
* The build task dependencies are managed by Gradle.
* The Frontend CSS is managed by Angular Material and Bootstrap
* Tests are written using JUnit and Karma.

# Pre conditions to run the application
1. Java 11 (https://www.oracle.com/java/technologies/javase/jdk11-archive-downloads.html)
2. Have Node, NPM, and Angular 14 installed. (https://nodejs.org/en/download/) and (https://angular.io/guide/setup-local)
3. Have Docker installed with MySQL running on a local docker. (https://hub.docker.com/_/mysql) The port is 13306, username and password is root.

# Steps to Run the application
1. To start the backend, you can either use the IDE configuration or use the terminal. 
Via terminal, go to the backend directory and type ```./gradlew bootRun```
2. To start the frontend, you can use the IDE configuration or use the terminal. 
Via terminal, go to the frontend directory and use ```npm start```
3. When both are up, you can navigate to http://localhost:4200 to view the application dashboard.

# Idea behind the application design
* To have a leasing contract, we need customers and vehicles. So, before we can add leasing contracts, we must populate our database with customers and vehicles.
* The Vehicle Management Page will enable viewing vehicles, adding vehicles and editing vehicles. Pagination is added for better experience.
* The Customer Management Page will enable viewing customers, adding customers, and editing customers. Pagination is added for better experience
* Once we have some customers and vehicles, we can create contracts in the Contract Management Page.

* In the Contract Management Page, we can view contracts, add contracts, and edit contracts. Pagination is added for better experience.
* One can only add a contract only when there are vehicle available. Since each vehicle can be associated with only one leasing contract, we have to omit assigned vehicles. And when there are no vehicles left, an error message is shown.
* To find the customer, a basic filter on the last name is developed. And if we have multiple customers with the same name, then we can differentiate them with the birthdate.
* To find a vehicle easily, a basic filter option is developed to filter by brand. Since, assigned vehicles are omitted, these will only show the available ones.

# Further Improvements
* Enable sorting and filtering of customers, vehicles and contracts.
* For the sake of the demo application, the brands and models are populated in the frontend. Ideally, these values should be from the DB so that we can easily add/edit them.
* Write e2e tests
