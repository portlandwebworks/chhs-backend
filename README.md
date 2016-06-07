# chhs-backend [![Build Status](https://travis-ci.org/portlandwebworks/chhs-backend.svg?branch=develop)](https://travis-ci.org/portlandwebworks/chhs-backend)

***Requirements:***

* Java 8
* Maven 3.x (must be able to run `mvn` on the command line)
* MySQL or MariaDB installed locally

***To Run:***

Create a new database and user in MySQL for local development. Name does not matter as you will configure that next.

Copy `application.yaml.example` to `application.yaml` and modify database parameters to match your local development setup. Assuming port numbers are the same the only thing you will have to change is the database name on the end of the URL and the username and password values.

Then you can run the application. Database structure will be migrated at startup.

```
mvn spring-boot:run
```

Server runs on port 8090, you can view the documentation for the endpoints at: http://localhost:8090/api/swagger.json

