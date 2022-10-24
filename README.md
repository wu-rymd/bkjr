# Kaiserschmarrn

Team repo for Advanced Software Engineering (COMS W4156) / Fall 2022 / team Kaiserschmarrn

## Quick start

- Run `./mvnw spring-boot:run`
- Go to `http://localhost:8080/accounts/<ACCOUNT_ID>/balance`

## Swagger API Documentation
- Visit [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html) after running the service

## Style checker

- We use the _checkstyle_ plugin from Maven to enforce a consistent style.
- To generate a report of style errors, run `mvn checkstyle:checkstyle`
  - **A report is generated at [/target/site/checkstyle.html](https://github.com/wu-rymd/kaiserschmarrn/blob/main/checkstyle/checkstyle.html)**
  - A report was copied to a new `/checkstyle/` directory for T3 requirements compliance, since the `/target` directory is git-ignored.

## Useful Links

[https://spring.io/guides/gs/rest-service/](https://spring.io/guides/gs/rest-service/)
[https://www.javaguides.net/2019/01/springboot-postgresql-jpa-hibernate-crud-restful-api-tutorial.html](https://www.javaguides.net/2019/01/springboot-postgresql-jpa-hibernate-crud-restful-api-tutorial.html)
