# chhs-backend [![Build Status](https://travis-ci.org/portlandwebworks/chhs-backend.svg?branch=develop)](https://travis-ci.org/portlandwebworks/chhs-backend)

***Requirements:***

* Java 8
* Maven 3.x (must be able to run `mvn` on the command line)
* MySQL 5.6.x or 5.7.x

***To Run:***

Create a new database and user in MySQL for local development. Name does not matter as you will configure that next.

Copy `application.yaml.example` to `application.yaml` and modify database parameters to match your local development setup. Assuming port numbers are the same the only thing you will have to change is the database name on the end of the URL and the username and password values.

Then you can run the application. 

```
mvn spring-boot:run
```

By default the server runs on port `8090`, and you can view the documentation for the endpoints [here](http://localhost:8090/api/swagger.json).

***Database:***

The database schema is currently controlled by [Liquibase](http://www.liquibase.org/), and it's integration with Spring Boot. On startup the application will automatically run any outstanding migrations. 

***Application Notes:***

On first startup a new, default, user is created with the email of `alice@caseworker.com`. When this happens you will see a log message telling you the users randomly generated password. This should be copied down if you wish to login as this test user.





